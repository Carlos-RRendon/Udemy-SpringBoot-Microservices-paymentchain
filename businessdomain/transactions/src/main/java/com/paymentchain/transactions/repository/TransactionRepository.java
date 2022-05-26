/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.paymentchain.transactions.repository;

import com.paymentchain.transactions.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("SELECT t FROM Transaction t WHERE t.ibanAccount = ?1")
    public List<Transaction> findByIbanAccount(String ibanAccount);
}
