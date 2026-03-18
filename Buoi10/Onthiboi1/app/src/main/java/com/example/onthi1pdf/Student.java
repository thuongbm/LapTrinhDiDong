package com.example.onthi1pdf;

public class Student {
    private int id;
    private String name;
    private int MathScore;
    private int PhysicsScore;
    private int ChemistryScore;

    public Student(int id, String name, int mathScore, int chemistryScore, int physicsScore) {
        this.id = id;
        this.name = name;
        this.MathScore = mathScore;
        this.ChemistryScore = chemistryScore;
        this.PhysicsScore = physicsScore;
    }

    public int calculateSumScore() {
        return MathScore + PhysicsScore + ChemistryScore;
    }

    public String getName() { return name; }
    public int getId() { return id; }
}
