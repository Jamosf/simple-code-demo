cmake_minimum_required(VERSION 3.6)
project(string_rank)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES string_rank.cpp)
add_executable(string_rank ${SOURCE_FILES})