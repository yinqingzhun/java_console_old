package com.yqz.console.tech.parallel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DocumentSearch {
	public static void main(String[] args) throws IOException {
		if (args == null || args.length == 0)
			args = new String[] { "E:\\code\\AutoGlobalVideo\\youku", "class" };
		WordCounter wordCounter = new WordCounter();
		Folder folder = Folder.fromDirectory(new File(args[0]));
//
//		long s1 = new Date().getTime();
//		System.out.println(wordCounter.countOccurrencesOnSingleThread(folder, args[1]));
//		
//		long s2 = new Date().getTime();
//		System.out.println("times by singleTread: " + (s2 - s1));

		long s3 = new Date().getTime();
		System.out.println(new DocumentSearch().countOccurrencesInParallel(folder, args[1]));

		long s4 = new Date().getTime();
		System.out.println("times by parallel: " + (s4 - s3));

	}

	private final ForkJoinPool forkJoinPool = new ForkJoinPool();

	public Long countOccurrencesInParallel(Folder folder, String searchedWord) {
		return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
	}

	class FolderSearchTask extends RecursiveTask<Long> {
		private final Folder folder;
		private final String searchedWord;

		FolderSearchTask(Folder folder, String searchedWord) {
			super();
			this.folder = folder;
			this.searchedWord = searchedWord;
		}

		@Override
		protected Long compute() {
			long count = 0L;
			List<RecursiveTask<Long>> forks = new LinkedList<>();
			for (Folder subFolder : folder.getSubFolders()) {
				FolderSearchTask task = new FolderSearchTask(subFolder, searchedWord);
				forks.add(task);
				task.fork();
			}
			for (Document document : folder.getDocuments()) {
				DocumentSearchTask task = new DocumentSearchTask(document, searchedWord);
				forks.add(task);
				task.fork();
			}
			for (RecursiveTask<Long> task : forks) {
				count = count + task.join();
			}
			return count;
		}
	}

	class DocumentSearchTask extends RecursiveTask<Long> {
		private final Document document;
		private final String searchedWord;

		DocumentSearchTask(Document document, String searchedWord) {
			super();
			this.document = document;
			this.searchedWord = searchedWord;
		}

		@Override
		protected Long compute() {
			
			return new WordCounter().occurrencesCount(document, searchedWord);
		}
	}

	static class WordCounter {

		String[] wordsIn(String line) {
			return line.trim().split("(\\s|\\p{Punct})+");
		}

		Long occurrencesCount(Document document, String searchedWord) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long count = 0;
			for (String line : document.getLines()) {
				for (String word : wordsIn(line)) {
					if (searchedWord.equals(word)) {
						count = count + 1;
					}
				}
			}
			return count;
		}

		Long countOccurrencesOnSingleThread(Folder folder, String searchedWord) {
			long count = 0;
			for (Folder subFolder : folder.getSubFolders()) {
				count = count + countOccurrencesOnSingleThread(subFolder, searchedWord);
			}
			for (Document document : folder.getDocuments()) {
				count = count + occurrencesCount(document, searchedWord);
			}
			return count;
		}

	}

	static class Folder {
		private final List<Folder> subFolders;
		private final List<Document> documents;

		Folder(List<Folder> subFolders, List<Document> documents) {
			this.subFolders = subFolders;
			this.documents = documents;
		}

		List<Folder> getSubFolders() {
			return this.subFolders;
		}

		List<Document> getDocuments() {
			return this.documents;
		}

		static Folder fromDirectory(File dir) throws IOException {
			List<Document> documents = new LinkedList<>();
			List<Folder> subFolders = new LinkedList<>();
			for (File entry : dir.listFiles()) {
				if (entry.isDirectory()) {
					subFolders.add(Folder.fromDirectory(entry));
				} else {
					documents.add(Document.fromFile(entry));
				}
			}
			return new Folder(subFolders, documents);
		}
	}

	static class Document {
		private final List<String> lines;

		Document(List<String> lines) {
			this.lines = lines;
		}

		List<String> getLines() {
			return this.lines;
		}

		static Document fromFile(File file) throws IOException {
			List<String> lines = new LinkedList<>();
			if (file.isFile() && file.getName().endsWith(".java")) {
				System.out.println(file.getName());
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line = reader.readLine();
					while (line != null) {
						lines.add(line);
						line = reader.readLine();
					}
				}
			}
			return new Document(lines);
		}
	}

}
