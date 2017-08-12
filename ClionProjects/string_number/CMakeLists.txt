cmake_minimum_required(VERSION 3.6)
project(string_number)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(string_number ${SOURCE_FILES})