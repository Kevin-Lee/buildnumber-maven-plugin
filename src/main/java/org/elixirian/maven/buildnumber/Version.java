/**
 *
 */
package org.elixirian.maven.buildnumber;

import org.elixirian.kommonlee.collect.immutable.ImmutableList;
import org.elixirian.kommonlee.type.checkable.EmptinessCheckable;
import org.elixirian.kommonlee.type.checkable.LengthCheckable;
import org.elixirian.kommonlee.type.checkable.NotEmptinessCheckable;
import org.elixirian.kommonlee.type.checkable.SizeCheckable;

/**
 * <pre>
 *     ___  _____                                _____
 *    /   \/    /_________  ___ ____ __ ______  /    /   ______  ______
 *   /        / /  ___ \  \/  //___// //     / /    /   /  ___ \/  ___ \
 *  /        \ /  _____/\    //   //   __   / /    /___/  _____/  _____/
 * /____/\____\\_____/   \__//___//___/ /__/ /________/\_____/ \_____/
 * </pre>
 * 
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-14)
 */
public interface Version extends LengthCheckable, SizeCheckable, EmptinessCheckable,
    NotEmptinessCheckable, Comparable<Version>
{

  ImmutableList<Integer> getVersionNumbers();

  @Override
  int hashCode();

  @Override
  boolean equals(final Object version);

  @Override
  String toString();
}
