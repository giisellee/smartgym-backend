package com.smartgym.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ATENDENTE")
public class Atendente extends Funcionario { }