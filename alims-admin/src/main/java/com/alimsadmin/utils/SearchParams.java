package com.alimsadmin.utils;

public class SearchParams {
    private String academicYearId;
    private String gradeId;
    private String sectionId;
    private String classRoomId;
    private String examId;
    private String subjectId;
    private String fromDate;
    private String toDate;
    private String teacherId;
    private String studentId;
    private String searchText;
    private Long minValue;
    private Long maxValue;

    public SearchParams() {}

    public SearchParams(String academicYearId, String gradeId, String sectionId, String classRoomId, String examId, String subjectId, String teacherId, String studentId, String searchText) {
        this.academicYearId = academicYearId;
        this.gradeId = gradeId;
        this.sectionId = sectionId;
        this.classRoomId = classRoomId;
        this.examId = examId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.searchText = searchText;
    }

    public SearchParams(String academicYearId, String gradeId, String sectionId, String classRoomId, String examId, String subjectId, String fromDate, String toDate, String teacherId, String studentId, String searchText) {
        this.academicYearId = academicYearId;
        this.gradeId = gradeId;
        this.sectionId = sectionId;
        this.classRoomId = classRoomId;
        this.examId = examId;
        this.subjectId = subjectId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.searchText = searchText;
    }

    public SearchParams(String academicYearId, String gradeId, String sectionId, String classRoomId, String examId, String subjectId, String fromDate, String toDate, String teacherId, String studentId, String searchText, Long minValue, Long maxValue) {
        this.academicYearId = academicYearId;
        this.gradeId = gradeId;
        this.sectionId = sectionId;
        this.classRoomId = classRoomId;
        this.examId = examId;
        this.subjectId = subjectId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.searchText = searchText;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getAcademicYearId() { return academicYearId; }

    public String getGradeId() { return gradeId; }

    public String getSectionId() { return sectionId; }

    public String getClassRoomId() { return classRoomId; }

    public String getExamId() { return examId; }

    public String getTeacherId() { return teacherId; }

    public String getStudentId() { return studentId; }

    public String getSearchText() { return searchText; }

    public String getSubjectId() { return subjectId; }

    public Long getMinValue() {
        return minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }
}
