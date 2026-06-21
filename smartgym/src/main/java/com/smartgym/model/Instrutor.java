package com.smartgym.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INSTRUTOR")
public class Instrutor extends Funcionario { }