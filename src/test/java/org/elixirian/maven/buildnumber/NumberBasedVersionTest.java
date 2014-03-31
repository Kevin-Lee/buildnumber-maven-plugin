package org.elixirian.maven.buildnumber;

import static org.assertj.core.api.Assertions.*;
import static org.elixirian.kommonlee.collect.immutable.ImmutableLists.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.elixirian.kommonlee.collect.immutable.ImmutableList;
import org.elixirian.kommonlee.test.CauseCheckableExpectedException;
import org.elixirian.kommonlee.test.CommonTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class NumberBasedVersionTest
{
  @Rule
  public CauseCheckableExpectedException expectedException = CauseCheckableExpectedException.none();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testHashCode()
  {
    /* given */
    final int expected = Arrays.asList(1, 2, 109)
        .hashCode();
    final Version version = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final int actual = version.hashCode();

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testHashCode2()
  {
    /* given */
    final int expected = Arrays.asList(1, 2)
        .hashCode();
    final Version version = NumberBasedVersion.newInstance("1.2.0-SNAPSHOT");

    /* when */
    final int actual = version.hashCode();

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testVersion() throws Exception
  {
    /* expected */
    expectedException.expect(IllegalAccessException.class);

    /* when */
    CommonTestHelper.newConstructorTester(NumberBasedVersion.class, this)
        .mustBePrivate()
        .parameterTypeAndValue(String.class, null)
        .parameterTypeAndValue(ImmutableList.class, listOf(1, 2, 109))
        .parameterTypeAndValue(int.class, listOf(1, 2, 109).hashCode())
        .test();

    /* otherwise-fail */
    fail();
  }

  @Test
  public final void testLength()
  {
    /* given */
    final int expected = 3;
    final Version onlyNumberBasedVersion = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final int actual = onlyNumberBasedVersion.length();

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testSize()
  {
    /* given */
    final int expected = 3;
    final Version onlyNumberBasedVersion = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final int actual = onlyNumberBasedVersion.size();

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testIsNotEmpty()
  {
    /* given */
    final Version version = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final boolean actual = version.isNotEmpty();

    /* then */
    assertThat(actual).isTrue();
  }

  @Test
  public final void testIsNotEmpty2()
  {
    /* given */
    final Version version = NumberBasedVersion.newInstance("");

    /* when */
    final boolean actual = version.isNotEmpty();

    /* then */
    assertThat(actual).isFalse();
  }

  @Test
  public final void testIsEmpty()
  {
    /* given */
    final Version version = NumberBasedVersion.newInstance("");

    /* when */
    final boolean actual = version.isEmpty();

    /* then */
    assertThat(actual).isTrue();
  }

  @Test
  public final void testIsEmpty2()
  {
    /* given */
    final Version version = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final boolean actual = version.isEmpty();

    /* then */
    assertThat(actual).isFalse();
  }

  @Test
  public final void testGetVersionNumbers()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 109);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.2.109-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers2()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 9);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.2.009-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers3()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 109);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.02.109-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers4()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 9);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.2.009-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers5()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 0);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.2.0-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers6()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 0, 0);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.0.0-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetVersionNumbers7()
  {
    /* given */
    final ImmutableList<Integer> expected = listOf(1, 2, 900);

    /* when */
    final ImmutableList<Integer> actual =
      NumberBasedVersion.getVersionNumbers("1.2.900-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo()
  {
    /* given */
    final int expected = 0;
    final Version version1 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo2()
  {
    /* given */
    final int expected = -1;
    final Version version1 = NumberBasedVersion.newInstance("1.2.108-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo3()
  {
    /* given */
    final int expected = 1;
    final Version version1 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.108-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo4()
  {
    /* given */
    final int expected = -1;
    final Version version1 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.110-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo5()
  {
    /* given */
    final int expected = -1;
    final Version version1 = NumberBasedVersion.newInstance("1.2.99-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.100-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testCompareTo6()
  {
    /* given */
    final int expected = -1;
    final Version version1 = NumberBasedVersion.newInstance("1.2.100-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.0900-SNAPSHOT");

    /* when */
    final int actual = version1.compareTo(version2);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testEqualsObject()
  {
    /* given */
    final Version version1 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final boolean actual = version1.equals(version2);

    /* then */
    assertThat(actual).isTrue();
  }

  @Test
  public final void testEqualsObject2()
  {
    /* given */
    final Version version1 = NumberBasedVersion.newInstance("1.2.109.RELEASE");
    final Version version2 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");

    /* when */
    final boolean actual = version1.equals(version2);

    /* then */
    assertThat(actual).isTrue();
  }

  @Test
  public final void testEqualsObject3()
  {
    /* given */
    final Version version1 = NumberBasedVersion.newInstance("1.2.109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.108-SNAPSHOT");

    /* when */
    final boolean actual = version1.equals(version2);

    /* then */
    assertThat(actual).isFalse();
  }

  @Test
  public final void testEqualsObject4()
  {
    /* given */
    final Version version1 = NumberBasedVersion.newInstance("1.2.0109-SNAPSHOT");
    final Version version2 = NumberBasedVersion.newInstance("1.2.108-SNAPSHOT");

    /* when */
    final boolean actual = version1.equals(version2);

    /* then */
    assertThat(actual).isFalse();
  }

  @Test
  public final void testNewInstanceStringString()
  {
    /* given */
    final ImmutableList<Integer> expectedVersionNumbers = listOf(1, 0, 1);

    /* when */
    final Version actual = NumberBasedVersion.newInstance("1.0.1-SNAPSHOT", NumberBasedVersion.DOT);

    /* then */
    assertThat(actual.getVersionNumbers()).isEqualTo(expectedVersionNumbers);
    assertThat(actual.length()).isEqualTo(expectedVersionNumbers.length());
  }

  @Test
  public final void testNewInstanceStringString2()
  {
    /* given */
    final ImmutableList<Integer> expectedVersionNumbers = listOf(1);

    /* when */
    final Version actual = NumberBasedVersion.newInstance("1.0.1-SNAPSHOT", ",");

    /* then */
    assertThat(actual.getVersionNumbers()).isEqualTo(expectedVersionNumbers);
    assertThat(actual.length()).isEqualTo(expectedVersionNumbers.length());
  }

  @Test
  public final void testNewInstanceString()
  {
    /* given */
    final ImmutableList<Integer> expectedVersionNumbers = listOf(1, 0, 1);

    /* when */
    final Version actual = NumberBasedVersion.newInstance("1.0.1-SNAPSHOT");

    /* then */
    assertThat(actual.getVersionNumbers()).isEqualTo(expectedVersionNumbers);
    assertThat(actual.length()).isEqualTo(expectedVersionNumbers.length());
  }

  @Test
  public final void testNewInstanceString2()
  {
    /* given */
    final ImmutableList<Integer> expectedVersionNumbers = listOf(1);

    /* when */
    final Version actual = NumberBasedVersion.newInstance("1,0,1,SNAPSHOT");

    /* then */
    assertThat(actual.getVersionNumbers()).isEqualTo(expectedVersionNumbers);
    assertThat(actual.length()).isEqualTo(expectedVersionNumbers.length());
  }

  @Test
  public final void testToString()
  {
    /* given */
    final String expected = "1.0.1-SNAPSHOT";

    /* when */
    final Version actual = NumberBasedVersion.newInstance("1.0.1-SNAPSHOT");

    /* then */
    assertThat(actual.toString()).isEqualTo(expected);
  }

  @Test
  public final void testToString2()
  {
    /* given */
    final String expected = "1.0.1-SNAPSHOT";

    /* when */
    final Version actual = NumberBasedVersion.newInstance("  1.0.1-SNAPSHOT  ");

    /* then */
    assertThat(actual.toString()).isEqualTo(expected);
  }

  @Test
  public final void testToString3()
  {
    /* given */
    final String expected = "1.0.1-SNAPSHOT";

    /* when */
    final Version actual = NumberBasedVersion.newInstance("  \t1.0.1-SNAPSHOT  \t\n");

    /* then */
    assertThat(actual.toString()).isEqualTo(expected);
  }

}
