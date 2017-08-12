cmake_minimum_required(VERSION 3.6)
project(crime)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES crime.cpp)
add_executable(crime ${SOURCE_FILES})