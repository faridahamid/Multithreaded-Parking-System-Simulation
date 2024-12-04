# Multithreaded-Parking-System-Simulation
# Overview

This project is a simulation of a parking system using Java, showcasing the use of threads and semaphores to manage a limited number of parking spots across three gates. The simulation ensures concurrency management, realistic car arrivals, and detailed status reporting.

# Objectives

Thread Synchronization: Efficiently manage access to parking spots using semaphores.

Concurrency Management: Handle concurrent car arrivals and departures without conflicts or errors.

Simulation Realism: Simulate real-world scenarios with cars arriving at specific times and staying for specified durations.

Status Reporting: Log activities and provide detailed reports, including the total number of cars served and the current parking status.

# Features
1) Parking Lot: A parking lot with 4 available spots.

2) Multiple Gates: Three gates (Gate 1, Gate 2, Gate 3) for cars to enter.

3) Car Threads: Each car is represented by a thread managing its parking lifecycle.
   
4) Semaphore Management: Use semaphores to control access to the limited parking spots and ensure no race conditions.
   
5) Logging and Reporting: Detailed logs of car activities (arrival, parking, waiting, departure). and a final report summarizing the number of cars served and the status of the parking lot.
