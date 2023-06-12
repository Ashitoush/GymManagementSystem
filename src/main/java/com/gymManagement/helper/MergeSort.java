package com.gymManagement.helper;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class MergeSort<T> {
    public List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        List<T> temp = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            temp.add(null);
        }
        return mergeSortHelper(list, temp, 0, list.size() - 1, comparator);
    }

    private List<T> mergeSortHelper(List<T> list, List<T> temp, int left, int right, Comparator<T> comparator) {
        if (left >= right) {
            return list;
        }
        int mid = left + (right - left) / 2;
        mergeSortHelper(list, temp, left, mid, comparator);
        mergeSortHelper(list, temp, mid + 1, right, comparator);
        return merge(list, temp, left, mid, right, comparator);
    }

    private List<T> merge(List<T> list, List<T> temp, int left, int mid, int right, Comparator<T> comparator) {
        for (int i = left; i <= right; i++) {
            temp.set(i, list.get(i));
        }
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (comparator.compare(temp.get(i), temp.get(j)) <= 0) {
                list.set(k++, temp.get(i++));
            } else {
                list.set(k++, temp.get(j++));
            }
        }
        while (i <= mid) {
            list.set(k++, temp.get(i++));
        }
        while (j <= right) {
            list.set(k++, temp.get(j++));
        }
        return list;
    }
}
