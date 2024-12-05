plugins {
    id("java")
}

group = "org.jenson"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

dependencies {}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}
tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
}