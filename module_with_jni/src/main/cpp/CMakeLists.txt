# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.

project("sample")

set(CMAKE_CXX_STANDARD 17)

set(LINKER_FLAGS "-Wl,--hash-style=both")
set(CXX_FLAGS "-fdata-sections -ffunction-sections -fno-exceptions -fno-rtti -fno-threadsafe-statics")

if (CMAKE_BUILD_TYPE STREQUAL "Debug")
    add_definitions(-DDEBUG)
    set(CXX_FLAGS "${CXX_FLAGS} -O0")
else ()
    set(CXX_FLAGS "${CXX_FLAGS} -O2 -fvisibility=hidden -fvisibility-inlines-hidden")
    set(LINKER_FLAGS "${LINKER_FLAGS} -Wl,-exclude-libs,ALL -Wl,--gc-sections -flto")
endif ()

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${CXX_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_MODULE_LINKER_FLAGS "${CMAKE_MODULE_LINKER_FLAGS} ${LINKER_FLAGS}")

add_library(
        sample # Sets the name of the library.
        SHARED # Sets the library as a shared library.

        # Provides a relative path to source file(s).
        sample.cpp
)

target_link_libraries(sample log)
