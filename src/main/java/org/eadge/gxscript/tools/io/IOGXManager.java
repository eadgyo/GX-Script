package org.eadge.gxscript.tools.io;

import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.data.io.EGX;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by eadgyo on 05/03/17.
 *
 * Save and load EGX, compiledGXScript, rawGXScript
 */
public class IOGXManager
{
    protected static IOGXManager iogxManager = null;

    public EGX loadEGX(String path)
    {
        Object object = getObject(path);

        if (object instanceof EGX)
            return (EGX) object;

        return null;
    }

    public CompiledGXScript loadCompiledGXScript(String path)
    {
        Object object = getObject(path);

        if (object instanceof CompiledGXScript)
            return (CompiledGXScript) object;

        return null;
    }

    public RawGXScript loadRawGXScript(String path)
    {
        Object object = getObject(path);

        if (object instanceof RawGXScript)
            return (RawGXScript) object;

        return null;
    }

    public void saveEGX(String path, EGX egx)
    {
        saveObject(egx, path);
    }

    public void saveCompiledGXScript(String path, CompiledGXScript compiledGXScript)
    {
        saveObject(compiledGXScript, path);
    }

    public void saveRawGXScript(String path, RawGXScript rawGXScript)
    {
        saveObject(rawGXScript, path);
    }

    public ArrayList<String> getAllFilesName(String Directory, boolean canCreate)
    {
        ArrayList<String> files  = new ArrayList<String>();
        File              folder = new File(Directory);
        if (folder.exists())
        {
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null)
            {
                for (File listOfFile : listOfFiles)
                {
                    if (listOfFile.isFile())
                    {
                        files.add(listOfFile.getName());
                    }
                }
            }
        }
        else if(canCreate)
        {
            folder.mkdir();
        }
        return files;
    }
    public boolean isFolderExisting(String Directory)
    {
        File folder = new File(Directory);
        return folder.exists();
    }
    public boolean createFolder(String Directory)
    {
        // Create folder if not existing
        File folder = new File(Directory);
        if (!folder.exists())
        {
            folder.mkdir();
            return true;
        }
        return false;
    }

    public boolean isFileExisting(String Directory)
    {
        File file = new File(Directory);
        return file.exists() && file.isFile();
    }

    public void saveObject(Object object, String directory)
    {
        try
        {
            FileOutputStream f_out = new FileOutputStream(directory);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject(object);
            obj_out.close();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public Object getObject(String directory)
    {
        Object obj = null;

        if(this.isFileExisting(directory))
        {
            try
            {
                FileInputStream   f_in   = new FileInputStream(directory);
                ObjectInputStream obj_in = new ObjectInputStream(f_in);
                obj = obj_in.readObject();
                obj_in.close();
            }
            catch (IOException | ClassNotFoundException e1)
            {
                e1.printStackTrace();
            }
        }
        return obj;
    }

    public static IOGXManager getInstance()
    {
        if (iogxManager == null)
            iogxManager = new IOGXManager();
        return iogxManager;
    }
}
