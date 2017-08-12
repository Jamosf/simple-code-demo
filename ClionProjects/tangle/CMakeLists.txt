cmake_minimum_required(VERSION 3.6)
project(tangle)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(tangle ${SOURCE_FILES})