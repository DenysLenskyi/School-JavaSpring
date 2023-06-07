package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.springframework.stereotype.Component;

@Component
public class SchoolCache {

    private long maxCourseId;
    private long minCourseId;
    private long maxGroupId;
    private long minGroupId;
    private long maxStudentId;
    private long minStudentId;

    public long getMaxCourseId() {
        return maxCourseId;
    }

    public void setMaxCourseId(long maxCourseId) {
        this.maxCourseId = maxCourseId;
    }

    public long getMinCourseId() {
        return minCourseId;
    }

    public void setMinCourseId(long minCourseId) {
        this.minCourseId = minCourseId;
    }

    public long getMaxGroupId() {
        return maxGroupId;
    }

    public void setMaxGroupId(long maxGroupId) {
        this.maxGroupId = maxGroupId;
    }

    public long getMinGroupId() {
        return minGroupId;
    }

    public void setMinGroupId(long minGroupId) {
        this.minGroupId = minGroupId;
    }

    public long getMaxStudentId() {
        return maxStudentId;
    }

    public void setMaxStudentId(long maxStudentId) {
        this.maxStudentId = maxStudentId;
    }

    public long getMinStudentId() {
        return minStudentId;
    }

    public void setMinStudentId(long minStudentId) {
        this.minStudentId = minStudentId;
    }
}