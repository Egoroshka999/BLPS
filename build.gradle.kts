import org.gradle.api.file.DuplicatesStrategy

plugins {
    java
    war
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.lab"
version = "0.0.1-SNAPSHOT"


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.3.3"

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")
//    compileOnly("org.springframework.boot:spring-boot-starter-tomcat")
//    implementation("org.jboss.slf4j:slf4j-jboss-logmanager:2.0.1.Final")
//    runtimeOnly("org.jboss.logmanager:jboss-logmanager:3.1.2.Final")
    configurations.all {
        exclude (group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
//    implementation("org.springframework.boot:spring-boot-starter-data-rest") {
//        exclude (group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
//        exclude (group = "org.springframework.boot", module = "spring-boot-starter-logging")
//    }
//    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.modulith:spring-modulith-starter-core")
//    implementation("org.springframework.modulith:spring-modulith-starter-jpa")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
//    compileOnly("org.projectlombok:lombok")
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
//    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
//    runtimeOnly("org.postgresql:postgresql")
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
//    annotationProcessor("org.projectlombok:lombok")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
//    testImplementation("com.h2database:h2")
//    testImplementation("org.springframework.security:spring-security-test")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("jakarta.resource:jakarta.resource-api:2.1.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.4")
}

dependencyManagement {
    imports {
//        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources")
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


