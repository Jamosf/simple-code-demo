cmake_minimum_required(VERSION 3.6)
project(linkedlist)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp mergeOfTwoList.cpp)
add_executable(linkedlist ${SOURCE_FILES})