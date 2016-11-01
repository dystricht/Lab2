package com.dystricht.Lab2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class GravitationalSystem {

	public final double GRAVCONST = 6.67384e-11;
	public ArrayList<Body> bodies;
	public static double[] cameraPos = { 0, 0 };
	public static Random r = new Random();

	public GravitationalSystem(ArrayList<Body> bodyList) {

		bodies = bodyList;
		for (Body b : this.bodies) {
			if (b.isStatic) {
				cameraPos = b.scaleXandY(Constants.pixelsPerMeter);
				break;
			}
		}
		
		for(Body b : this.bodies){
			b.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
		}
	}

	public void addBody(Body newBody) {

		bodies.add(newBody);
	}

	public void update(double timestep) {

		// find acceleration of Body a caused by (all other)Body b, for each
		// Body a.
		for (Body a : bodies) {

			double[] totalAccels = { 0, 0 };
			double[] adder = new double[2];

			for (Body b : bodies) {
				// make sure no bodies affect their own velocity.
				if (b.equals(a)) {
					continue;
				} else {
					// compute gravitational effects from every other body
					// double[] accelerations = computeAcceleration(a, b); //OLD
					// way of updating accelerations
					// System.out.println("we're accelerating");

					// accels[i] = computeAcceleration(a, b);

					// a.setAccel(computeAcceleration(a, b));

					adder = computeAcceleration(a, b);

					totalAccels = adder;

					a.addAccel(adder);

				}

			}
		}

		for (Body a : bodies) {
			a.updateVelocity(timestep);
			a.updatePosition(timestep);
		}
	}

	public void draw(double cx, double cy, double pixelsPerMeter) {

		for (Body b : bodies) {
			b.draw(cx, cy, pixelsPerMeter);
		}
	}

	// implementation of acceleration of Body a caused by Body b
	public double[] computeAcceleration(Body a, Body b) { // must return 2
															// values: x and y
															// accelerations

		// distance between bodies
		double distance = distanceBetween(a, b);

		if (distance < 9e7) {
			return new double[] { 0, 0 };
		}

		double distX = a.x - b.x;
		double distY = a.y - b.y;
		// double distX = Math.abs(a.x - b.x);
		// double distY = Math.abs(a.y - b.y); // distance WRT x and y (distance
		// is
		// abs value)
		double acceleration = (GRAVCONST * b.mass) / Math.pow(distance, 2); // formula
																			// for
																			// acceleration
																			// of
																			// one
																			// body
																			// by
																			// another

		double ax = -(acceleration * distX) / distance; // acceleration with
														// respect to X
		double ay = -(acceleration * distY) / distance; // acceleration with
														// respect to Y

		double[] accelerations = { ax, ay };

		return accelerations;

	}

	// distance between bodies - the distance formula
	public double distanceBetween(Body a, Body b) {

		double distance = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));

		return distance;
	}
}
