package com.telran.philosophers.task;

/*
"The Dining Philosophers" Task – Resolve the Deadlock in class Philosophers

The history:
-----------
The Dining Philosophers problem is one of the classic problems used to describe synchronization issues
in a multi-threaded environment and illustrate techniques for solving them. 
Dijkstra first formulated this problem and presented it regarding computers accessing tape drive peripherals.
The present formulation was given by Tony Hoare, who is also known for inventing the quicksort sorting algorithm. 

The legend:
-----------
There are five silent philosophers sitting around a circular table, spending their lives eating and thinking.
There are five forks for them to share. And to be able to eat, a philosopher needs to have forks in both his hands. 
After eating, he puts both of them down and then they can be picked by another philosopher who repeats the same cycle.

The problem:
-----------
We can get the following situation: each of the Philosophers has acquired his left fork, but can’t acquire his 
right fork, because his neighbour has already acquired it. 
This situation is commonly known as the CIRCULAR WAIT and is one of the conditions that results in a deadlock
and prevents the progress of the system. 

The task:
---------
The goal is to come up with a scheme/protocol that helps the philosophers achieve their goal of eating and thinking 
without getting starved to death.

 */
public class Philosophers {
    public static void main(String[] args) throws Exception {

        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];
            if (i != philosophers.length-1) {
            philosophers[i] = new Philosopher(leftFork, rightFork);
            } else {
                philosophers[i] = new Philosopher(rightFork, leftFork);
            }
            
            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
        
    }
    static class Philosopher implements Runnable {

        private final Object leftFork;
        private final Object rightFork;

        public Philosopher(Object leftFork, Object rightFork) {
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " " + action);
            Thread.sleep(((int) (Math.random() * 10)));
        }


        @Override
        public void run() {
            try {
                while (true) {
                    // thinking
                    doAction(System.nanoTime() + ": Thinking");
 //                  if (Thread.currentThread().getName().equalsIgnoreCase("Philosopher " + ))
                    synchronized (leftFork) {
                        doAction(System.nanoTime() + ": Picked up left fork");
                        synchronized (rightFork) {
                            // eating
                            doAction(System.nanoTime() + ": Picked up right fork - eating");

                            doAction(System.nanoTime() + ": Put down right fork");
                        }
                        // Back to thinking
                        doAction(System.nanoTime() + ": Put down left fork. Back to thinking");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
