package com.greplin.interval;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * Base class for numeric intervals. Most functionality is implemented in
 * subclasses to avoid autoboxing and improve performance.
 * @param <T> value type
 */
public abstract class NumericInterval<T extends Number> {

  /**
   * @return the start of the interval, boxed.
   */
  public abstract T getBoxedStart();

  /**
   * @return the end of the interval, boxed.
   */
  public abstract T getBoxedEnd();


  @Override
  @SuppressWarnings("unchecked") // Safe because this class is abstract.
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NumericInterval<T> interval = (NumericInterval<T>) o;
    return this.getBoxedEnd().equals(interval.getBoxedEnd())
        && this.getBoxedStart().equals(interval.getBoxedEnd());
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(this.getBoxedStart(), this.getBoxedEnd());
  }


  @Override
  public String toString() {
    return this.getClass().getSimpleName()
        + "{" + this.getBoxedStart() + '-' + this.getBoxedEnd() + '}';
  }


  /**
   * @return this interval formatted as a string.
   */
  public String asString() {
    return this.getBoxedStart() + "-" + this.getBoxedEnd();
  }


  /**
   * @return this interval formatted as a 2 element list.
   */
  public ImmutableList<T> asList() {
    return ImmutableList.of(this.getBoxedStart(), this.getBoxedEnd());
  }


  /**
   * @return this interval formatted as a 2 element list of strings.
   */
  public ImmutableList<String> asStringList() {
    return ImmutableList.of(
        this.getBoxedStart().toString(), this.getBoxedEnd().toString());
  }

}
