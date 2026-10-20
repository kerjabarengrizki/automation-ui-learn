plugins {
    id("java")
}


group = "org.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}


dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.25.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.9.2")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")


    testImplementation("org.testng:testng:7.10.2")
    testImplementation("com.aventstack:extentreports:5.1.2")
}


tasks.test {
    useTestNG {
        // Run the TestNG suite, which defines parallel="methods" + thread-count.
        suites("src/test/resources/testng.xml")

        // Let TestNG manage the threads inside a single JVM. Optionally override the
        // suite's thread-count from the command line, e.g. -PthreadCount=5.
        (project.findProperty("threadCount") as String?)?.toIntOrNull()?.let {
            threadCount = it
        }
    }

    // Keep one JVM so @BeforeSuite/config load once; TestNG handles parallelism inside it.
    maxParallelForks = 1

    // Pass through -Denv=... so ConfigManager can pick the environment.
    systemProperty("env", System.getProperty("env", ""))

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = false
    }
}

