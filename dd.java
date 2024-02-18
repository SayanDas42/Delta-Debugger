import java.util.*;
import java.io.*;
import java.lang.*;

public class dd
{
    static int step=1,cval=1;
    static void createDiff()  throws Exception
    {
        List<String> commands = new ArrayList<String>();
        commands.add("gcc");
        commands.add("diff.c");
        commands.add("-o");
        commands.add("out");
        ProcessBuilder pb=new ProcessBuilder(commands);
        Process p=pb.start();
        p.waitFor();
        commands.clear();
        commands.add("./out");
        pb=new ProcessBuilder(commands);
        p=pb.start();
        p.waitFor();
    }
    static int printDiff() throws Exception
    {
        File file = new File("patch1.patch");
        BufferedReader br=new BufferedReader(new FileReader(file));
        String st="";
        int c=0;
        while((st=br.readLine())!=null)
        {
            if(st.charAt(0)=='@')
            {
                System.out.println(st);
                c++;
            }
        }
        System.out.println("# of Total Change sets is = "+c);
        return c;
    }

    static List<String> createChanges(int c) throws Exception
    {
        List<String> commands = new ArrayList<String>();
        commands.add("splitpatch");
        commands.add("--hunks");
        commands.add("patch1.patch");
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process p= pb.start();
        p.waitFor();
        List<String> s=new ArrayList<String>();
        for(int i=0;i<c;i++)
        {
            String st="",str="";
            if(i<10)
                st="00"+i;
            else if(i>=10&&i<100)
                st="0"+i;
            else
                st=""+i;
            st="."+st+".patch";
            str=(i+1)+".patch";
            s.add(str);
            commands.clear();
            commands.add("mv");
            commands.add(st);
            commands.add(str);
            pb = new ProcessBuilder(commands);
            p=pb.start();
            p.waitFor();
        }
        return s;
    }

    static String passOrFail() throws Exception
    {
        String s="Pass";
        try
        {
            List<String> commands = new ArrayList<String>();
            commands.add("javac");
            commands.add("-d");
            commands.add("file1v1_3");
            commands.add("file1v1_3/file1v1.java");
            ProcessBuilder pb=new ProcessBuilder(commands);
            Process p=pb.start();
            p.waitFor();
            commands.clear();
            commands.add("java");
            commands.add("-cp");
            commands.add("file1v1_3");
            commands.add("file1v1");
            commands.add("5");
            commands.add("0");
            commands.add("division");
            pb=new ProcessBuilder(commands);
            p=pb.start();
            p.waitFor();
            if(p.exitValue()!=0)
                throw new ArithmeticException();
        }
        catch(Exception e)
        {
            s="Fail";
        }
        return s;
    }

   static void copyOriginal() throws Exception
    {
        List<String> commands = new ArrayList<String>();
        commands.add("gcc");
        commands.add("copyoriginal.c");
        commands.add("-o");
        commands.add("out");
        ProcessBuilder pb=new ProcessBuilder(commands);
        Process p=pb.start();
        p.waitFor();
        commands.clear();
        commands.add("./out");
        pb=new ProcessBuilder(commands);
        p=pb.start();
        p.waitFor();
    }

    static void combineChanges(List<String> changeset) throws Exception
    {
        List<String> commands = new ArrayList<String>();
        commands.add("gcc");
        commands.add("combinechanges.c");
        commands.add("-o");
        commands.add("out");
        ProcessBuilder pb=new ProcessBuilder(commands);
        Process p=pb.start();
        p.waitFor();
        commands.clear();
        commands.add("./out");
        for(int i=0;i<changeset.size();i++)
            commands.add(changeset.get(i));
        pb=new ProcessBuilder(commands);
        p=pb.start();
        p.waitFor();
        applyPatch();
    }

    static void applyPatch() throws Exception
    {
        List<String> commands = new ArrayList<String>();
        commands.add("gcc");
        commands.add("applypatch.c");
        commands.add("-o");
        commands.add("out");
        ProcessBuilder pb=new ProcessBuilder(commands);
        Process p=pb.start();
        p.waitFor();
        commands.clear();
        commands.add("./out");
        pb=new ProcessBuilder(commands);
        p=pb.start();
        p.waitFor();
    }

    static int DelDebug(List<String> c, List<String> r) throws Exception
    {
        if(c.size()==1)
        {
            System.out.println("Changes where bugs occured: "+"["+c.get(0).charAt(0)+"]");
            return 0;
        }
        List<String> c1=c.subList(0, c.size()/2);
        List<String> c2=c.subList(c.size()/2, c.size());
        List<String> c1r=new ArrayList<String>();
        List<String> c2r=new ArrayList<String>();
        int c1s=0,c2s=0;
        c1r=Union(c1,r);
        copyOriginal();
        combineChanges(c1r);
        if(passOrFail()=="Fail")
        {
            System.out.print("Step "+(step++)+": "+"c_"+(cval++)+": ");
            for(int i=0;i<c1r.size();i++){
                if(Character.isDigit(c1r.get(i).charAt(1)))
                    System.out.print(c1r.get(i).charAt(0)+""+c1r.get(i).charAt(1)+" ");
                else
                    System.out.print(c1r.get(i).charAt(0)+" ");
            }
            System.out.print("Fail");
            System.out.println();
            if(DelDebug(c1,r)==0)
                return 0;
        }
        else if(passOrFail()=="Pass")
        {
            System.out.print("Step "+(step++)+": "+"c_"+(cval++)+": ");
            for(int i=0;i<c1r.size();i++){
                if(Character.isDigit(c1r.get(i).charAt(1)))
                    System.out.print(c1r.get(i).charAt(0)+""+c1r.get(i).charAt(1)+" ");
                else
                    System.out.print(c1r.get(i).charAt(0)+" ");
            }
            System.out.print("Pass");
            System.out.println();
            c1s=1;
        }
        c2r=Union(c2,r);
        copyOriginal();
        combineChanges(c2r);
        if(passOrFail()=="Fail")
        {
            System.out.print("Step "+(step++)+": "+"c_"+(cval++)+": ");
            for(int i=0;i<c2r.size();i++){
                if(Character.isDigit(c2r.get(i).charAt(1)))
                    System.out.print(c2r.get(i).charAt(0)+""+c2r.get(i).charAt(1)+" ");
                else
                    System.out.print(c2r.get(i).charAt(0)+" ");
            }
            System.out.print("Fail");
            System.out.println();
            if(DelDebug(c2,r)==0)
                return 0;
        }
        else if(passOrFail()=="Pass")
        {
            System.out.print("Step "+(step++)+": "+"c_"+(cval++)+": ");
            for(int i=0;i<c2r.size();i++){
                if(Character.isDigit(c2r.get(i).charAt(1)))
                    System.out.print(c2r.get(i).charAt(0)+""+c2r.get(i).charAt(1)+" ");
                else
                    System.out.print(c2r.get(i).charAt(0)+" ");
            }
            System.out.print("Pass");
            System.out.println();
            c2s=1;
        }
        if(c1s==1&&c2s==1)
        {
            if(DelDebug(c1,Union(c2,r))==0)
                return 0;
            if(DelDebug(c2,Union(c1,r))==0)
                return 0;
        }
        return 1;
    }

    static List<String> Union(List<String> s1, List<String> s2)
    {
        List<String> s3=new ArrayList<String>();
        s3.addAll(s1);
        s3.addAll(s2);
        return s3;
    }

    public static void main(String args[]) throws Exception
    {
        System.out.println("Delta Debugging Project");
        createDiff();
        int c=printDiff();
        List<String> s=createChanges(c);
        List<String> r=new ArrayList<String>();
        DelDebug(s,r);
    }
}