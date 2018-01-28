package com.hawallen.moodpath.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.hawallen.moodpath.MyShareActivity;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;

public class ShareService implements RequestListener {

	private Activity mContext;
//	private AlertDialog dialog;
//	private String shareString;

//	/** ��ȡ��Phone���ֶ� **/
//	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
//	/** ��ϵ����ʾ��� **/
//	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
//	/** �绰���� **/
//	private static final int PHONES_NUMBER_INDEX = 1;
//	/** ͷ��ID **/
//	private static final int PHONES_PHOTO_ID_INDEX = 2;
//	/** ��ϵ�˵�ID **/
//	private static final int PHONES_CONTACT_ID_INDEX = 3;
//	/** ��ϵ����� **/
//	private ArrayList<String> mContactsName = new ArrayList<String>();
//	/** ��ϵ�˺��� **/
//	private ArrayList<String> mContactsNumber = new ArrayList<String>();
//	/** ��ϵ��ͷ�� **/
//	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

	public ShareService(Activity context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void share2sina(String shareString, String picPath) {
		Intent intent = new Intent();
		intent.putExtra("shareString", shareString);
		intent.putExtra("picPath", picPath);
		intent.setClass(mContext, MyShareActivity.class);
		mContext.startActivity(intent);

	}

	public void share2contact(String shareString) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + ""));
		intent.putExtra("sms_body", shareString);
		mContext.startActivity(intent);
	}

//	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/
//	private void getPhoneContacts() {
//		ContentResolver resolver = mContext.getContentResolver();
//		// ��ȡ�ֻ���ϵ��
//		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
//		if (phoneCursor != null) {
//			while (phoneCursor.moveToNext()) {
//				// �õ��ֻ����
//				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
//				// ���ֻ����Ϊ�յĻ���Ϊ���ֶ� ���ǰѭ��
//				if (TextUtils.isEmpty(phoneNumber))
//					continue;
//				// �õ���ϵ�����
//				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
//				// �õ���ϵ��ID
//				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
//				// �õ���ϵ��ͷ��ID
//				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
//				// �õ���ϵ��ͷ��Bitamp
//				Bitmap contactPhoto = null;
//				// photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и��������ͷ�������һ��Ĭ�ϵ�
//				if (photoid > 0) {
//					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
//					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//					contactPhoto = BitmapFactory.decodeStream(input);
//				} else {
//					contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
//				}
//				mContactsName.add(contactName);
//				mContactsNumber.add(phoneNumber);
//				mContactsPhonto.add(contactPhoto);
//			}
//			phoneCursor.close();
//		}
//	}
//
//	/** �õ��ֻ�SIM����ϵ������Ϣ **/
//	private void getSIMContacts() {
//		ContentResolver resolver = mContext.getContentResolver();
//		// ��ȡSims����ϵ��
//		Uri uri = Uri.parse("content://icc/adn");
//		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
//		if (phoneCursor != null) {
//			while (phoneCursor.moveToNext()) {
//				// �õ��ֻ����
//				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
//				// ���ֻ����Ϊ�յĻ���Ϊ���ֶ� ���ǰѭ��
//				if (TextUtils.isEmpty(phoneNumber))
//					continue;
//				// �õ���ϵ�����
//				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
//				// Sim����û����ϵ��ͷ��
//				mContactsName.add(contactName);
//				mContactsNumber.add(phoneNumber);
//			}
//			phoneCursor.close();
//		}
//	}

//	private class MyHandler extends Handler {
//		public void handleMessage(android.os.Message msg) {
//			dialog.cancel();
//			if (mContactsName.isEmpty()) {
//				Toast.makeText(mContext, "û����ϵ��", Toast.LENGTH_LONG).show();
//			} else {
//				dialog.setTitle("ѡ����ϵ��");
//				View contactList = View.inflate(mContext, R.layout.contact_list, null);
//				LinearLayout llContactList = (LinearLayout) contactList.findViewById(R.id.ll_contact_list);
//				for (int i = 0; i < mContactsName.size(); i++) {
//					View contact = View.inflate(mContext, R.layout.contact, null);
//					ImageView imageView = (ImageView) contact.findViewById(R.id.iv_contact_image);
//					TextView tvName = (TextView) contact.findViewById(R.id.tv_contact_name);
//					TextView tvNumber = (TextView) contact.findViewById(R.id.tv_contact_number);
//					imageView.setImageBitmap(mContactsPhonto.get(i));
//					tvName.setText(mContactsName.get(i));
//					tvNumber.setText(mContactsNumber.get(i));
//					contact.setId(i);
//					contact.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.cancel();
//							Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mContactsNumber.get(v.getId())));
//							intent.putExtra("sms_body", shareString);
//							mContext.startActivity(intent);
//						}
//					});
//					llContactList.addView(contact);
//				}
//				dialog.setView(contactList);
//				dialog.show();
//			}
//		}
//	}

	@Override
	public void onComplete(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException e) {
		// TODO Auto-generated method stub

	}
}
