package com.modou.coeus.ability.git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-26 17:05
 **/
public class GitCompareClassNameHandler implements GitCompareHandlerInterface{

    private List<String> className = new ArrayList<>();

    @Override
    public void doInvoke(GitCompareContext gitCompareContext) {
        List<String> className = new ArrayList<>();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(out);
            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            df.setRepository(gitCompareContext.getRepository());
            List<DiffEntry> diffEntryList = gitCompareContext.getDiffEntryList();
            for (DiffEntry diffEntry : diffEntryList){
                df.format(diffEntry);
                String dfText = out.toString();

                FileHeader fileHeader = df.toFileHeader(diffEntry);
                className.add(getNewPath(fileHeader.getNewPath()));
                List<? extends HunkHeader> hunks = fileHeader.getHunks();
                for (HunkHeader hunkHeader : hunks){

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.className = className;
    }

    public List<String> getCompareClassName(){
        return this.className;
    }

    private static String getNewPath(String newPath){
        return newPath.substring(newPath.indexOf("java")+5).replace(".java","").replace("/",".");
    }
}
