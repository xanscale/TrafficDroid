package it.localhost.trafficdroid.service;

import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.exception.BadConfException;
import it.localhost.trafficdroid.exception.GenericException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

public class PersistanceService {
	public static final String tdData = "trafficData";
	private static final String versionMismatch = "Version Mismatch";

	public static void store(Context context, MainDTO dto) throws GenericException {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(tdData, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(dto);
		} catch (FileNotFoundException e) {
			throw new GenericException(e);
		} catch (IOException e) {
			throw new GenericException(e);
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				// Do nothing
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}

	public static MainDTO retrieve(Context context) throws GenericException, BadConfException {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(tdData);
			ois = new ObjectInputStream(fis);
			MainDTO dlctask = (MainDTO) ois.readObject();
			if (dlctask.getVersionCode() != Utility.getVersionCode(context))
				throw new BadConfException(versionMismatch);
			return dlctask;
		} catch (FileNotFoundException e) {
			throw new GenericException(e);
		} catch (StreamCorruptedException e) {
			throw new GenericException(e);
		} catch (IOException e) {
			throw new GenericException(e);
		} catch (ClassNotFoundException e) {
			throw new GenericException(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// Do nothing
			}
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
}