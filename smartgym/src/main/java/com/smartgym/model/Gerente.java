package com.smartgym.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GERENTE")
public class Gerente extends Funcionario { }