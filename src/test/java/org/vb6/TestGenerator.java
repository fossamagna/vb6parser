/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package org.vb6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.Trees;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vb6.VisualBasic6Parser.StartRuleContext;

public class TestGenerator {

	private final static File INPUT_DIRECTORY = new File("src/test/resources/org/vb6");

	private static final String JAVA_EXTENSION = ".java";

	private final static Logger LOG = LogManager.getLogger(TestGenerator.class);

	private final static File OUTPUT_DIRECTORY = new File("src/test/java/org/vb6");

	private final static String OUTPUT_FILE_SUFFIX = "Test";

	private static final String TREE_EXTENSION = ".tree";

	/**
	 * To be removed, as soon as the VB6 grammar does not require NEWLINEs and
	 * WS anymore
	 */
	@Deprecated
	private static String cleanFileTree(final String inputFileTree) {
		final String inputFileTreeNoNewline = inputFileTree.replace("\\r", "").replace("\\n", "");
		final String inputFileTreeNoWhitespace = inputFileTreeNoNewline.replaceAll("[ ]+", " ").replace(" )", ")");
		final String result = inputFileTreeNoWhitespace;
		return result;
	}

	public static String firstToUpper(final String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static void generateTestClass(final File vb6InputFile, final File outputDirectory, final String packageName)
			throws IOException {
		final String inputFilename = getInputFilename(vb6InputFile);
		final File outputFile = new File(outputDirectory + "/" + inputFilename + OUTPUT_FILE_SUFFIX + JAVA_EXTENSION);

		LOG.info("Creating unit test {}.", outputFile);
		final boolean createdNewFile = outputFile.createNewFile();

		if (createdNewFile) {
			final PrintWriter pWriter = new PrintWriter(new FileWriter(outputFile));
			final String vb6InputFileName = vb6InputFile.getPath().replace("\\", "/");

			pWriter.write("package " + packageName + ";\n");
			pWriter.write("\n");
			pWriter.write("import java.io.File;\n");
			pWriter.write("\n");
			pWriter.write("import org.junit.Test;\n");
			pWriter.write("import org.vb6.runner.VbParseTestRunner;\n");
			pWriter.write("import org.vb6.runner.impl.VbParseTestRunnerImpl;\n");
			pWriter.write("\n");
			pWriter.write("public class " + inputFilename + "Test {\n");
			pWriter.write("\n");
			pWriter.write("	@Test\n");
			pWriter.write("	public void test() throws Exception {\n");
			pWriter.write("		final File inputFile = new File(\"" + vb6InputFileName + "\");\n");
			pWriter.write("		final VbParseTestRunner runner = new VbParseTestRunnerImpl();\n");
			pWriter.write("		runner.parseFile(inputFile);\n");
			pWriter.write("	}\n");
			pWriter.write("}");

			pWriter.flush();
			pWriter.close();
		}
	}

	public static void generateTestClasses(final File inputDirectory, final File outputDirectory,
			final String packageName) throws IOException {
		final String outputDirectoryPath = outputDirectory.getPath();

		if (inputDirectory.isDirectory()) {
			// for each of the files in the directory
			for (final File inputDirectoryFile : inputDirectory.listFiles()) {
				// if the file is a VB6 relevant file
				if (inputDirectoryFile.isFile() && !inputDirectoryFile.isHidden()
						&& (isClazzModule(inputDirectoryFile) || isStandardModule(inputDirectoryFile))) {
					generateTestClass(inputDirectoryFile, outputDirectory, packageName);
					generateTreeFile(inputDirectoryFile, inputDirectory);
				}
				// else, if the file is a directory
				else if (inputDirectoryFile.isDirectory()) {
					final File subInputDirectory = inputDirectoryFile;
					final String subInputDirectoryName = subInputDirectory.getName();

					if (!".".equals(subInputDirectoryName) && !"..".equals(subInputDirectoryName)) {
						/*
						 * determine the output directory, where test classes
						 * should be placed
						 */
						final File subOutputDirectory = new File(outputDirectoryPath + "/" + subInputDirectoryName);
						subOutputDirectory.mkdirs();

						// determine the package name of test classes
						final String subPackageName = packageName + "." + subInputDirectoryName;

						generateTestClasses(subInputDirectory, subOutputDirectory, subPackageName);
					}
				}
			}
		}
	}

	public static void generateTreeFile(final File vb6InputFile, final File outputDirectory) throws IOException {
		final File outputFile = new File(outputDirectory + "/" + vb6InputFile.getName() + TREE_EXTENSION);

		LOG.info("Creating tree file {}.", outputFile);
		final boolean createdNewFile = outputFile.createNewFile();

		if (createdNewFile) {
			final InputStream inputStream = new FileInputStream(vb6InputFile);
			final VisualBasic6Lexer lexer = new VisualBasic6Lexer(new ANTLRInputStream(inputStream));
			final CommonTokenStream tokens = new CommonTokenStream(lexer);
			final VisualBasic6Parser parser = new VisualBasic6Parser(tokens);
			final StartRuleContext startRule = parser.startRule();
			final String inputFileTree = Trees.toStringTree(startRule, parser);
			final String cleanedInputFileTree = cleanFileTree(inputFileTree);

			final PrintWriter pWriter = new PrintWriter(new FileWriter(outputFile));

			pWriter.write(cleanedInputFileTree);
			pWriter.flush();
			pWriter.close();
		}
	}

	protected static String getInputFilename(final File inputFile) {
		final String result = firstToUpper(FilenameUtils.removeExtension(inputFile.getName()));
		return result;
	}

	protected static boolean isClazzModule(final File inputFile) {
		final String extension = FilenameUtils.getExtension(inputFile.getName()).toLowerCase();
		return "cls".equals(extension);
	}

	protected static boolean isStandardModule(final File inputFile) {
		final String extension = FilenameUtils.getExtension(inputFile.getName()).toLowerCase();
		return "bas".equals(extension);
	}

	public static void main(final String[] args) throws IOException {
		generateTestClasses(INPUT_DIRECTORY, OUTPUT_DIRECTORY, "org.vb6");
	}
}
