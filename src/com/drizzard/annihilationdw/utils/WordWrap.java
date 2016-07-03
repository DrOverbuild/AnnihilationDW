package com.drizzard.annihilationdw.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jasper on 12/27/15.
 */
public class WordWrap {

	public static List<StringBuilder> wordWrap(String s, int width) {
		List<StringBuilder> lines = new ArrayList<>();
		String[] words = s.split(" ");
		if (words.length > 1) {
			lines.add(new StringBuilder(words[0]));
			words = Arrays.copyOfRange(words, 1, words.length);

			for (String word : words) {
				if ((lines.get(lines.size() - 1).toString() + " " + word).length() < width) {
					lines.get(lines.size() - 1).append(" ").append(word);
				} else {
					if (word.length() > 20) {
						String colors = ChatColor.getLastColors(lines.get(lines.size() - 1).toString());
						lines.add(new StringBuilder(colors + word.substring(0, width - 1)));
						colors = ChatColor.getLastColors(lines.get(lines.size() - 1).toString());
						lines.add(new StringBuilder(colors + word.substring(width)));
					} else {
						String colors = ChatColor.getLastColors(lines.get(lines.size() - 1).toString());
						lines.add(new StringBuilder(colors + word));
					}
				}
			}
		}

		return lines;
	}

}
