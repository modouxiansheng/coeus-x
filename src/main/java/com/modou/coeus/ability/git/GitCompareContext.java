package com.modou.coeus.ability.git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-26 17:02
 **/
public class GitCompareContext {

    private List<DiffEntry> diffEntryList;

    private Repository repository;

    private String workingDirectory;

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public List<DiffEntry> getDiffEntryList() {
        return diffEntryList;
    }

    public void setDiffEntryList(List<DiffEntry> diffEntryList) {
        this.diffEntryList = diffEntryList;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
