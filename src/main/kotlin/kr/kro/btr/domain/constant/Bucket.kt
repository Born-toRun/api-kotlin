package kr.kro.btr.domain.constant

enum class Bucket(val bucketName: String, val isEncryption: Boolean) {
    PROFILE("profile", false),
    FEED("feed", false),
    CREW("crew", false)
}
