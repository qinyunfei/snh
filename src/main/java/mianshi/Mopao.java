package mianshi;

import java.util.Arrays;

import org.junit.Test;

public class Mopao {

	// 冒泡排序
	@Test
	public void name() {
		int a[] = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35,
				25, 53, 51 };
		int temp = 0;

		for (int i = 0; i < a.length - 1; i++) {
			for (int j = 0; j < a.length - 1 - i; j++) {
				if (a[j] > a[j + 1]) {
					temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}

			}
		}
		Arrays.sort(a);
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
	}

	// 合并排序
	@Test
	public void name2() {

		int s[] = { 1, 3, 4, 6, 8, 9, 12 };

		int b[] = { 0, 2, 5, 7, 10, 11 };
		int a[] = Arrays.copyOf(s, s.length + b.length);
		
		for (int i = s.length; i < a.length; i++) {
			a[i] = b[i - s.length];
			System.out.print(a[i] + " ");
		}
		Arrays.sort(a);
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
	}

}
