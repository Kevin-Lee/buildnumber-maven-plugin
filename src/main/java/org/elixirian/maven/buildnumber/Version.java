/**
 *
 */
package org.elixirian.maven.buildnumber;

import static org.elixirian.kommonlee.util.Objects.*;
import static org.elixirian.kommonlee.util.Strings.*;
import static org.elixirian.kommonlee.validation.Assertions.*;

import java.util.regex.Pattern;

import org.elixirian.kommonlee.collect.immutable.ImmutableList;
import org.elixirian.kommonlee.collect.immutable.ImmutableLists;
import org.elixirian.kommonlee.type.checkable.EmptinessCheckable;
import org.elixirian.kommonlee.type.checkable.LengthCheckable;
import org.elixirian.kommonlee.type.checkable.NotEmptinessCheckable;
import org.elixirian.kommonlee.type.checkable.SizeCheckable;
import org.elixirian.kommonlee.type.functional.Function1;

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
public class Version implements LengthCheckable, SizeCheckable, EmptinessCheckable, NotEmptinessCheckable,
    Comparable<Version>
{
  public static final Version EMPTY_VERSION = new Version("", ImmutableLists.<Integer> emptyList(), 0);

  /*
   * versionString = "12345" -> 12345 versionString = "12345A" -> 12345 versionString = "12345A11B" -> 12345
   * versionString = "0012345A" -> 12345
   */
  private static final Function1<String, Integer> TAKE_ONLY_FIRST_DIGIT_PART = new Function1<String, Integer>() {
    @SuppressWarnings("boxing")
    @Override
    public Integer apply(final String versionString)
    {
      final int length = versionString.length();
      int position = length;
      for (int i = 0; i < length; i++)
      {
        final char c = versionString.charAt(i);
        if (!Character.isDigit(c))
        {
          position = i;
          break;
        }
      }
      try
      {
        /* @formatter:off */
        return length == position ?
                  Integer.parseInt(versionString) :
                  Integer.parseInt(versionString.substring(0, position));
        /* @formatter:on */
      }
      catch (final NumberFormatException e)
      {
        return Integer.valueOf(0);
      }
    }
  };

  public static final String DOT = Pattern.quote(".");

  private final String version;

  private final ImmutableList<Integer> versionNumbers;

  private final int hashCode;

  public Version(final String version, final ImmutableList<Integer> versionNumbers, final int hashCode)
  {
    this.version = version;
    this.versionNumbers = versionNumbers;
    this.hashCode = hashCode;
  }

  @Override
  public int length()
  {
    return versionNumbers.length();
  }

  @Override
  public int size()
  {
    return length();
  }

  @Override
  public boolean isNotEmpty()
  {
    return !isEmpty();
  }

  @Override
  public boolean isEmpty()
  {
    return versionNumbers.isEmpty();
  }

  public ImmutableList<Integer> getVersionNumbers()
  {
    return versionNumbers;
  }

  @Override
  public int compareTo(final Version version)
  {
    final int thisLength = length();
    final int thatLength = version.length();

    final int length = Math.min(thisLength, thatLength);

    for (int i = 0; i < length; i++)
    {
      final Integer thisNumber = versionNumbers.get(i);
      final Integer thatNumber = version.versionNumbers.get(i);
      final int compared = thisNumber.compareTo(thatNumber);
      if (0 != compared)
      {
        return compared;
      }
    }

    /* check the rest */
    if (thisLength > thatLength)
    {
      for (final Integer v : versionNumbers.subList(thatLength, thisLength))
      {
        @SuppressWarnings("boxing")
        final int n = v;
        if (0 != n)
        {
          return 1;
        }
      }
    }
    else if (thatLength > thisLength)
    {
      for (final Integer v : version.versionNumbers.subList(thisLength, thatLength))
      {
        @SuppressWarnings("boxing")
        final int n = v;
        if (0 != n)
        {
          return -1;
        }
      }
    }
    return 0;
  }

  @Override
  public int hashCode()
  {
    return hashCode;
  }

  @Override
  public boolean equals(final Object version)
  {
    if (this == version)
    {
      return true;
    }
    final Version that = castIfInstanceOf(Version.class, version);
    return null != that && 0 == compareTo(that);
  }

  private static ImmutableList<Integer> getVersionNumbers(final String version, final String separator)
  {
    /* @formatter:off */
      return version.isEmpty() ?
          ImmutableLists.<Integer> emptyList() :
          ImmutableLists.listOf(version
                               .split(separator))
                        .map(TAKE_ONLY_FIRST_DIGIT_PART);
      /* @formatter:on */
  }

  public static Version newInstance(final String version, final String separator)
  {
    assertNotNull(version, "version cannot be null! [version: %s]", version);
    final String trimmedVersion = nullSafeTrim(version);
    assertNotNull(separator, "separator cannot be null! [separator: %s]", separator);

    final ImmutableList<Integer> versionNumbers = getVersionNumbers(trimmedVersion, separator);
    if (versionNumbers.isEmpty())
    {
      return EMPTY_VERSION;
    }

    final int length = versionNumbers.length();
    int lastNotZero = -1;
    for (int i = length - 1; i >= 0; i--)
    {
      @SuppressWarnings("boxing")
      final int n = versionNumbers.get(i);
      if (n != 0)
      {
        lastNotZero = i;
        break;
      }
    }
    /* @formatter:off */
    final int hashCode = 0 <= lastNotZero ?
        versionNumbers.subList(0, lastNotZero)
        .hashCode() :
          versionNumbers.hashCode();
        /* @formatter:on */
    return new Version(trimmedVersion, versionNumbers, hashCode);
  }

  public static Version newInstance(final String version)
  {
    return newInstance(version, DOT);
  }

  @Override
  public String toString()
  {
    return isEmpty() ? "[EMPTY]" : version;
  }

}
