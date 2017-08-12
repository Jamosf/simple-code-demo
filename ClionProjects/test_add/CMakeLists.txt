cmake_minimum_required(VERSION 3.6)
project(test_add)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(test_add ${SOURCE_FILES})