package com.gibsoncodes.data.source

import android.annotation.SuppressLint
import android.app.usage.StorageStatsManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.util.TimeUnit
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.text.format.Formatter
import android.util.Log
import androidx.core.net.toUri
import com.gibsoncodes.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

/**
 * Class contains all the meat/methods used to load data needed by the app
 */
class DataSource (private val context:Context){
    suspend fun loadRecentFiles():List<RecentFiles> = withContext(Dispatchers.IO){
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED
    )
        val recentFiles = mutableListOf<RecentFiles>()
        /**
         * Media types as per the documentation
         * 2 == audio
         * 6 == document
         * 1 == image
         * 3 == video
         */
        // MediaStore.Files.getContentUri("external") get documents
        val selectionArgs = arrayOf(getDateWithinAPeriod().toString())
        // AND ${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (2,6,1,3)
        val selection ="${MediaStore.Files.FileColumns.DATE_ADDED} >= ? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (2,1,3) "
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        val query = context.contentResolver
            .query(MediaStore.Files.getContentUri("external"),
            projection, selection, selectionArgs, sortOrder)
        query?.use{
            cursor->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val fileName = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val fileSize = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateAddedFile = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val mediaType = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
            while(cursor.moveToNext()){
                val fileId = cursor.getLong(idColumn)
                val name = cursor.getString(fileName)
                val size = cursor.getInt(fileSize)
                val dateAdded = Date(cursor.getLong(dateAddedFile))
                val fileMediaType = cursor.getInt(mediaType)
                val uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"),
                fileId)
                val recentFile = RecentFiles(fileId,name, size, uri, dateAdded,
                fileMediaType)

                recentFiles.plusAssign(recentFile)
            }

        }
        Log.e("recent files list :" ,"${recentFiles.size}")
  return@withContext recentFiles

    }
    @SuppressLint("SimpleDateFormat")
    private fun getDateWithinAPeriod():Long{
        val calendar = Calendar.getInstance()
        val days = -2
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val date = Date(calendar.timeInMillis)
        // Sun Oct 25 11:36:26 GMT+03:00 2020
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

       return simpleDateFormat.let{
           val stringDate =it.format(date)
           Log.e("tag",stringDate)
           java.util.concurrent.TimeUnit.MICROSECONDS.toSeconds(it.parse(stringDate)?.time?:0)
       }



    }
    /**
     * Provides a sum of all the files per category i.e
     * audio, images, downloads etc
     */
    suspend fun loadFileSizes():TotalFileSizes= withContext(Dispatchers.IO){
        val audiosSize= loadAudioFilesFromDisc().sumBy { it.size!! }
        val videosList = loadVideosMedia().sumBy { it.size!! }
        val imagesList = loadImagesFromDisc().sumBy {it.imageSize  }
        val downloadFilesList = loadDownloadFilesFromDisc().sumBy { it.fileSize }
        return@withContext TotalFileSizes(downloadsFileSize = downloadFilesList,
        imagesFileSize = imagesList,audiosSize = audiosSize,
        videosSize = videosList)
    }
    /**
     * Get the storage volume of the device
     * total storage + usedUp storage
     * StorageManager is only available in Oreo and above thus
     * we have to use [StatFs] for android O>
     */
    @Suppress("DEPRECATION")
    suspend fun loadStorageStats(): StorageStatistics? = withContext(Dispatchers.IO){
        var storageStatistics: StorageStatistics?=null
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val storageStatsManager =
                context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val storageVolumes = storageManager.storageVolumes
            storageVolumes.forEach {
                val uuidString =  it.uuid
               val uuid = if (uuidString==null){
                   StorageManager.UUID_DEFAULT
               }else UUID.fromString(uuidString)
                val totalMemory = storageStatsManager.getTotalBytes(uuid)
                val freeMemory = storageStatsManager.getFreeBytes(uuid)
                val usedMemory = totalMemory-freeMemory
                storageStatistics=
                    StorageStatistics(
                        usedUpStorage = Formatter.formatShortFileSize(
                            context,
                            usedMemory
                        ), totalStorage = Formatter.formatShortFileSize(context, totalMemory)
                    )
            }

        }else{
          val statFs by lazy {
              val rootPath = Environment.getExternalStorageDirectory().absolutePath
              StatFs(rootPath)
          }
            val blockCount  by lazy{
                statFs.availableBlocksLong
            }
            val blockSize by lazy {
                statFs.blockSizeLong
            }
            val availableBlocks by lazy{
                statFs.availableBlocksLong
            }
            val totalMemory = (blockCount*blockSize).absoluteValue
            val freeMemory = (blockSize*availableBlocks).absoluteValue
            val usedUpMemory = (totalMemory-freeMemory).absoluteValue
            storageStatistics= StorageStatistics(
                usedUpStorage = Formatter.formatShortFileSize(
                    context,
                    usedUpMemory
                ), totalStorage = Formatter.formatShortFileSize(context, totalMemory)
            )

        }
        return@withContext storageStatistics
    }

/**
 * Loads the videos present in the user's phone
 */
suspend fun loadVideosMedia(): List<Videos> = withContext(Dispatchers.IO) {
    val videosList = mutableListOf<Videos>()
    val projections = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DATE_ADDED
    )
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
    val query = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projections, null, null, sortOrder
    )
    query?.use { cursor ->
        val id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val dateAdded = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
        while (cursor.moveToNext()) {
            val videoId = cursor.getLong(id)
            val videoName = cursor.getString(name)
            val videoSize = cursor.getInt(size)
            val videoDateAdded = Date(cursor.getLong(dateAdded))
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoId
            )
            videosList.plusAssign(
                Videos(
                    videoId,
                    videoName, videoSize, contentUri, videoDateAdded, createThumbnail(
                        context.contentResolver,
                        contentUri
                    )
                )
            )
        }
    }
    return@withContext videosList
}

/**
 * Loads the images present in the user's phone
 */
suspend fun loadImagesFromDisc():List<Images> = withContext(Dispatchers.IO){
    val imagesList = mutableListOf<Images> ()
    val projections = arrayOf(MediaStore.Images.Media._ID,
        MediaStore.Images.Media.TITLE,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_ADDED)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
    val query = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    projections, null, null, sortOrder)
    query?.use{
         val id= it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val name = it.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
        val size = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        val dateAdded = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

        Log.e("Data source tag", "Found ${it.count} images")
        while(it.moveToNext()){
            val imageId = it.getLong(id)
            val imageName = it.getString(name)
            val imageSize = it.getInt(size)
            val imageAdded = Date(it.getLong(dateAdded))
            val contentUri =ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageId)
            val imageModel = Images(
                imageId = imageId, imageName = imageName, imageSize = imageSize,
                contentUri = contentUri, dateAdded = imageAdded
            )
           Log.v("Data source tag", "added image:  $imageModel")
            imagesList+=(imageModel)
        }
        }
    Log.v("Tag data source", "Found ${imagesList.size} images")
    return@withContext imagesList.toList()
}

/**
 * Loads files present in the downloads folder
 * However, since the downloads table was introduced in Android Q and above
 * we need to walk(iterate recursively) the download folder via the (deprecated in Android Q< ) [Environment.getExternalStoragePublicDirectory] in order
 * to get all the files present in the downloads folder
 */
@Suppress("DEPRECATION")
suspend fun loadDownloadFilesFromDisc():List<Downloads> = withContext(Dispatchers.IO){
    val downloadFilesList = ArrayList<Downloads> ()
    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
        val projections = arrayOf(MediaStore.Downloads._ID,
            MediaStore.DownloadColumns.TITLE,
            MediaStore.DownloadColumns.SIZE,
            MediaStore.DownloadColumns.MIME_TYPE,
            MediaStore.DownloadColumns.DATE_ADDED)
        val sortOrder = "${MediaStore.Downloads.DATE_ADDED} ASC"
        val query = context.contentResolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        projections, null, null, sortOrder)
        query?.use{
            val id = it.getColumnIndexOrThrow(MediaStore.DownloadColumns._ID)
            val name = it.getColumnIndexOrThrow(MediaStore.DownloadColumns.TITLE)
            val mimeType = it.getColumnIndexOrThrow(MediaStore.DownloadColumns.MIME_TYPE)
            val dateAdded = it.getColumnIndexOrThrow(MediaStore.DownloadColumns.DATE_ADDED)
            val size = it.getColumnIndexOrThrow(MediaStore.DownloadColumns.SIZE)
            while(it.moveToNext()){
                val downloadFileId = it.getLong(id)
                val downloadFileName = it.getString(name)
                val downloadFileType=it.getString(mimeType)
                val downloadFileAdded = Date(it.getLong(dateAdded))
                val downloadFileSize = it.getInt(size)
                val contentUri = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                downloadFileId)

                val downloadFile = Downloads(
                    fileName = downloadFileName, fileSize = downloadFileSize,
                    fileExtension = downloadFileType, bitmap = createThumbnail(
                        context.contentResolver,
                        contentUri
                    ), dateAdded = downloadFileAdded,
                    uri = contentUri
                )
                downloadFilesList.plusAssign(downloadFile)
            }
        }
    }else{
        val downloadsFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val downloadFilesTree = File(downloadsFilePath.toString()).walk()
        downloadFilesTree.maxDepth(2)
            .filter { it.isFile }
            .forEach {
                val downloadFile = Downloads(
                    fileName =
                    it.name, fileSize = it.length().toInt(),
                    fileExtension = it.extension, dateAdded =
                    Date(it.lastModified()), uri = it.toUri(), bitmap = null
                )
                downloadFilesList.plusAssign(downloadFile)
                downloadFilesList.sortedBy { it.dateAdded }
            }
    }
    return@withContext downloadFilesList
}

/**
 * Loads the image files from disk
 */
suspend fun loadAudioFilesFromDisc():List<Audios> = withContext(Dispatchers.IO){
val audioFilesList = mutableListOf<Audios>()
val projections = arrayOf(MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.SIZE,
    MediaStore.Audio.Media.DATE_ADDED
)
val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} ASC"
val query = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projections,null, null,
 sortOrder)
query?.use{
    cursor->
    val id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val name = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val size = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
    val dateAdded = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
    while (cursor.moveToNext()) {
        val audioId = cursor.getLong(id)
        val audioName = cursor.getString(name)
        val audioSize = cursor.getInt(size)
        val audioDateAdded = Date(cursor.getLong(dateAdded))
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioId
        )
        val audioModel = Audios(
            id = audioId,
            name = audioName,
            size = audioSize,
            uri = contentUri,
            dateAdded = audioDateAdded,
            thumbnail = createThumbnail(context.contentResolver, contentUri)
        )
        audioFilesList.add(audioModel)
    }
    }
return@withContext audioFilesList

}

/**
 * Creates an thumbnail from the uri of the file given
 * Note:This might actually fail to produce any bitmap/thumbnail since
 * it all depends on the file
 */
private fun createThumbnail(resolver:ContentResolver, uri:Uri):Bitmap? {
    var bitmap: Bitmap? =null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(
            resolver,
            uri
        )
        bitmap = ImageDecoder.decodeBitmap(source)
        return bitmap
    }
    return bitmap
}

}