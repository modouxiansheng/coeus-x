package com.modou.coeus.ability.git;

import com.modou.coeus.domain.GitChangeDate;
import com.modou.coeus.node.Line;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-26 17:05
 **/
public class GitCompareChangeLineHandler implements GitCompareHandlerInterface{


    private List<GitChangeDate> gitChangeDates = new ArrayList<>();

    @Override
    public void doInvoke(GitCompareContext gitCompareContext) {
        List<GitChangeDate> gitChangeDates = new ArrayList<>();
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
                GitChangeDate changeDate = new GitChangeDate();
                changeDate.setClassName(getNewPath(fileHeader.getNewPath()));


                List<? extends HunkHeader> hunks = fileHeader.getHunks();
                for (HunkHeader hunkHeader : hunks){
                    EditList edits = hunkHeader.toEditList();
                    for (Edit edit : edits){
                        changeDate.addWithInit(new Line(edit.getBeginB(),edit.getEndB()));
                    }
                }
                gitChangeDates.add(changeDate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.gitChangeDates = gitChangeDates;
    }

    public List<GitChangeDate> getGitChangeDates(){
        return this.gitChangeDates;
    }

    private static String getNewPath(String newPath){
        return newPath.substring(newPath.indexOf("java")+5).replace(".java","").replace("/",".");
    }
}
