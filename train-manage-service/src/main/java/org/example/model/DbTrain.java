package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class DbTrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "train_num")
    private Long trainNum;

    @Column(name = "route_id")
    private Long routeId;

    public DbTrain(Long id, Long trainNum, Long routeId) {
        this.id = id;
        this.trainNum = trainNum;
        this.routeId = routeId;
    }

    public Long getId() {
        return id;
    }

    public DbTrain setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getTrainNum() {
        return trainNum;
    }

    public DbTrain setTrainNum(Long trainNum) {
        this.trainNum = trainNum;
        return this;
    }

    public Long getRouteId() {
        return routeId;
    }

    public DbTrain setRouteId(Long routeId) {
        this.routeId = routeId;
        return this;
    }
}
