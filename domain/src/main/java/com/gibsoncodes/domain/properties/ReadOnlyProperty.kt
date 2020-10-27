package com.gibsoncodes.domain.properties

/**
 * This allows us to split the different functions further
 * each into its different properties..the properties/ modules are the one which will
 * be dependended on by the view models rather than depending on the repo themselves
 * this makes addition of new features easy and reduces cluttering of functions in one place
 * It is read only because we are giving the results hence
 * ensuring a one way flow from source-> downstream
 */
interface ReadOnlyProperty <out Result>{
    suspend operator fun invoke():Result
}