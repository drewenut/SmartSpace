package de.ilimitado.smartspace.utils;

public final class StatisticUtils {
	private StatisticUtils() {
	}

	/**
	 * Computes the mean value for the argument array. Adds all values and
	 * divides them by the number of array elements.
	 * 
	 * @param values
	 *            Double array on which the mean is to be determined
	 * @return computed mean value
	 * @throws IllegalArgumentException
	 *             if the array has not at least one element
	 */
	public static Double computeMean(Double[] values) {
		return computeMean(values, 0, values.length);
	}

	/**
	 * Computes the mean value for some elements of the argument array. Adds all
	 * values and divides them by the number of array elements.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param offset
	 *            index of the first element to be used
	 * @param number
	 *            number of elements to be used
	 * @return computed mean value
	 * @throws IllegalArgumentException
	 *             if the array has not at least one element
	 */
	public static Double computeMean(Double[] values, int offset, int number) {
		if (number < 1) {
			throw new IllegalArgumentException(
					"The number of values to process must be one or larger.");
		}
		Double sum = 0.0;
		final int UNTIL = offset + number;
		do {
			sum += values[offset++];
		} while (offset != UNTIL);
		return sum / number;
	}

	/**
	 * Computes the standard deviation for the argument array of values.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @return computed standard deviation
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeStandardDeviation(Double[] values) {
		return computeStandardDeviation(values, 0, values.length);
	}

	/**
	 * Computes the standard deviation for the argument array of values. Reuses
	 * the mean value for that argument which must have been computed before.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param mean
	 *            the mean value for the array, possibly computed with a call to
	 *            {@link #computeMean(Double[])}.
	 * @return computed standard deviation
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeStandardDeviation(Double[] values, Double mean) {
		return computeStandardDeviation(values, 0, values.length, mean);
	}

	/**
	 * Computes the standard deviation for some of the argument array's values.
	 * If you already have computed a mean value using
	 * {@link #computeMean(Double[], int, int)}, better call
	 * {@link #computeStandardDeviation(Double[], int, int, Double)}. Otherwise,
	 * this method has to compute mean again.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param offset
	 *            first element to be used
	 * @param number
	 *            number of elements used starting at values[offset]
	 * @return computed standard deviation
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeStandardDeviation(Double[] values, int offset,
			int number) {
		Double mean = computeMean(values, offset, number);
		return computeStandardDeviation(values, 0, values.length, mean);
	}

	/**
	 * Computes the standard deviation for some of the argument array's values.
	 * Use this version of the method if you already have a mean value,
	 * otherwise this method must be computed again.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param offset
	 *            first element to be used
	 * @param number
	 *            number of elements used starting at values[offset]
	 * @param mean
	 *            value of the elements
	 * @return computed standard deviation
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeStandardDeviation(Double[] values, int offset,
			int number, Double mean) {
		return Math.sqrt(computeVariance(values, offset, number, mean));
	}

	/**
	 * Computes the variance for the argument array.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @return variance for the array elements
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeVariance(final Double[] values) {
		return computeVariance(values, 0, values.length);
	}

	/**
	 * Computes the variance for some of the argument array's values.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param mean
	 *            the mean for the array elements
	 * @return variance for the array elements
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeVariance(final Double[] values,
			final Double mean) {
		return computeVariance(values, 0, values.length, mean);
	}

	/**
	 * Computes the variance for some of the argument array's values. If you
	 * already have computed a mean value using
	 * {@link #computeMean(Double[], int, int)}, better call
	 * {@link #computeVariance(Double[], int, int, Double)}. Otherwise, this
	 * method has to compute mean again.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param offset
	 *            first element to be used
	 * @param number
	 *            number of elements used starting at values[offset]
	 * @return computed variance
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeVariance(final Double[] values, int offset,
			final int number) {
		Double mean = computeMean(values, offset, number);
		return computeVariance(values, 0, values.length, mean);
	}

	/**
	 * Computes the variance for some of the argument array's values. Use this
	 * version of the method in case mean has already been computed.
	 * 
	 * @param values
	 *            array from which elements are read
	 * @param offset
	 *            first element to be used
	 * @param number
	 *            number of elements used starting at values[offset]
	 * @param mean
	 *            the mean for the array elements
	 * @return computed variance
	 * @throws IllegalArgumentException
	 *             if the array has not at least two elements
	 */
	public static Double computeVariance(final Double[] values, int offset,
			final int number, final Double mean) {
		if (number < 2) {
			throw new IllegalArgumentException(
					"The number of values to process must be two or larger.");
		}
		Double sum = 0.0;
		final int UNTIL = offset + number;
		do {
			Double diff = values[offset++] - mean;
			sum += diff * diff;
		} while (offset != UNTIL);
		return sum / (number - 1);
	}
}
