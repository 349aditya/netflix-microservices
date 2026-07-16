package org.netflix.contentservice.model;

/*
 * Tracks the video processing lifecycle
 *
 *   Flow:-
 *
 *   PENDING ->  UPLOADED ->  ENCODING -> ENCODED -> READY -> FAILED
 * */

public enum VideoStatus {

    PENDING, //movie added but not uploaded yet
    UPLOADED, //video uploaded successfully to s3
    ENCODING, //FFmpeg is encoding the video
    ENCODED, //video encoding completed successfully
    READY, //HLS Playlist generated and video is ready to stream
    FAILED //Encoding failed

}
