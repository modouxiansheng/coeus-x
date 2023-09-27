package com.modou.coeus.ability.git;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-26 16:49
 **/
public class GitCompareAbility {

    private String url;

    private PassWord passWord;

    private String workingDirectory;

    private GitCompareHandlerInterface gitCompareHandler;

    private static final String BASE_DIRECTORY = "/Users/admin/temp";

    public GitCompareAbility(String url,GitCompareHandlerInterface gitCompareHandlerInterface,PassWord passWord) {
        this.url = url;
        this.gitCompareHandler = gitCompareHandlerInterface;
        workingDirectory = BASE_DIRECTORY + "/" +getWorkDicName(url);
        this.passWord = passWord;
        try {
            File file = new File(workingDirectory);
            if (!file.exists()){
                file.mkdir();
                Git.cloneRepository()
                        .setURI(url)
                        .setDirectory(new File(workingDirectory))
                        .setCredentialsProvider(getCredentialsProvider())
                        .call();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void invoke(String newBranchName,String oldBranchName){
        try (Git git = Git.open(new File(workingDirectory))){
            Repository repository = git.getRepository();
            gitCheckout(git,oldBranchName);
            gitPull(git,oldBranchName);
            gitCheckout(git,newBranchName);
            gitPull(git,newBranchName);
            //方法1：根据分支名称获取
            String currentBranch = repository.getBranch();
            ObjectId branchObjId = repository.resolve(currentBranch);

            //获取master分支的objectID
            ObjectId masterBranchObjId = repository.resolve(oldBranchName);

            //当前分支
            RevWalk revWalk = new RevWalk(repository);
            RevCommit revCommit = revWalk.parseCommit(branchObjId);

            //master分支
            RevCommit revCommitMaster = revWalk.parseCommit(masterBranchObjId);

            //from the commit we can build the tree which allows us to construct the TreeParser
            //当前分支信息
            RevTree revTree = revCommit.getTree();
            try(ObjectReader objReader = repository.newObjectReader();) {
                CanonicalTreeParser currentBranchTreeParser = new CanonicalTreeParser();
                currentBranchTreeParser.reset(objReader,revTree.getId());

                //master分支信息
                RevTree revTreeMaster = revCommitMaster.getTree();
                CanonicalTreeParser masterBranchTreeParser = new CanonicalTreeParser();
                masterBranchTreeParser.reset(objReader,revTreeMaster.getId());
                revWalk.dispose();

                List<DiffEntry> diffEntryList = git.diff().setOldTree(masterBranchTreeParser).setNewTree(currentBranchTreeParser).setShowNameAndStatusOnly(true).call();

                gitCompareHandler.doInvoke(initGitCompareContext(diffEntryList,repository));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public String getWorkingDirectory() {
        return workingDirectory;
    }

    private GitCompareContext initGitCompareContext(List<DiffEntry> diffEntryList, Repository repository){

        GitCompareContext gitCompareContext = new GitCompareContext();
        gitCompareContext.setDiffEntryList(diffEntryList);
        gitCompareContext.setRepository(repository);
        gitCompareContext.setWorkingDirectory(workingDirectory);
        return gitCompareContext;
    }

    private String getWorkDicName(String url){
        int lastSlashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');

        return url.substring(lastSlashIndex + 1, dotIndex);
    }

    private CredentialsProvider getCredentialsProvider(){

        return new UsernamePasswordCredentialsProvider(passWord.getUserName(),passWord.getPassWord());
    }

    private void gitCheckout(Git git,String name) throws GitAPIException {
        List<Ref> call = git.branchList().call();
        boolean createBranch = Boolean.TRUE;
        for (Ref ref : call) {
            if (ref.getName().contains(name)){
                createBranch = Boolean.FALSE;
            }
        }
        CheckoutCommand checkout = git.checkout();
        checkout.setName(name);
        checkout.setCreateBranch(createBranch);
        checkout.call();
    }

    private void gitPull(Git git,String name) throws GitAPIException {
        PullCommand pullCommand = git.pull();
        pullCommand.setRemoteBranchName(name);
        pullCommand.setCredentialsProvider(getCredentialsProvider());
        PullResult call = pullCommand.call();
        if (!call.isSuccessful()){
            throw new IllegalArgumentException("pull 失败");
        }
    }

    public void invokeCompile(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "clean", "install","-DskipTests");
            processBuilder.directory(new File(workingDirectory));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 读取输出流内容
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完毕
            int exitCode = process.waitFor();
            System.out.println("Command exited with code " + exitCode);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static class PassWord{

        private String userName;

        private String passWord;

        public PassWord(String userName, String passWord) {
            this.userName = userName;
            this.passWord = passWord;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassWord() {
            return passWord;
        }
    }

}
