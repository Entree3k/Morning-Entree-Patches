extension {
    name = "extensions/all/detection/signature/pms.mpe"
}

android {
    namespace = "morningentree.morphe.extension"
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
}
