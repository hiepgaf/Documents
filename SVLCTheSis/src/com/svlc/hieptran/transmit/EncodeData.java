package com.svlc.hieptran.transmit;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.svlcthesis.R;
import com.svlc.hieptran.reciever.IOLib;
import com.svlcthesis.activity.MainActivity;
import com.svlcthesis.activity.TransmitActivity;

public class EncodeData extends AsyncTask<Void, Void, ArrayList<Mat>> {
	private Mat c;
	ImageView im;
	int h, w;
	double start, end;
	Context mtv;
	int count_times = 0;
	Bitmap bmpend, bpend2;
	String data1;
	int i;
	String datfrfile;
	// String datvi,daten;
	ArrayList<String> datastring = new ArrayList<>();

	public EncodeData(int mh, int mw, Context tv, String mpath) {
		h = mh;
		w = mw;
		mtv = tv;
		datfrfile = toJAVA(mpath);
		bmpend = BitmapFactory.decodeResource(tv.getResources(),
				R.drawable.dsc);
		
//		 daten =
//		 "Let me begin with a sort of parable. Many years ago when I was on the staff of a great public school, we engaged a new swimming master. He was the most successful man in that capacity that we had had for years. Then one day it was discovered that he couldnt swim. He was standing at the edge of the swimming tank explaining the breast stroke to the boys in the water. He lost his balance and fell in. He was drowned. Or no, he wasnt drowned, I remember,--he was rescued by some of the pupils whom he had taught to swim. After he was resuscitated by the boys--it was one of the things he had taught them--the school dismissed him. Then some of the boys who were sorry for him taught him how to swim, and he got a new job as a swimming master in another place. But this time he was an utter failure. He swam well, but they said he couldnt teach. So his friends looked about to get him a new job. This was just at the time when the bicycle craze came in. They soon found the man a position as an instructor in bicycle riding. As he had never been on a bicycle in his life, he made an admirable teacher. He stood fast on the ground and said,  Now then, all you need is confidence.  Then one day he got afraid that he might be found out. So he went out to a quiet place and got on a bicycle, at the top of a slope, to learn to ride it. The bicycle ran away with him. But for the skill and daring of one of his pupils, who saw him and rode after him, he would have been killed. This story, as the reader sees, is endless. Suffice it to say that the man I speak of is now in an aviation school teaching people to fly. They say he is one of the best aviators that ever walked. According to all the legends and story books, the principal factor in success is perseverance. Personally, I think there is nothing in it. If anything, the truth lies the other way. There is an old motto that runs, If at first you dont succeed, try, try again. This is nonsense. It ought to read, If at first you dont succeed, quit, quit, at once.If you cant do a thing, more or less, the first time you try, you will never do it. Try something else while there is yet time. Let me illustrate this with a story. I remember, long years ago, at a little school that I attended in the country, we had a schoolmaster, who used perpetually to write on the blackboard, in a copperplate hand, the motto that I have just quoted: If at first you dont succeed,Try, try, again. He wore plain clothes and had a hard, determined face. He was studying for some sort of preliminary medical examination, and was saving money for a medical course. Every now and then he went away to the city and tried the examination: and he always failed. Each time he came back, he would write up on the blackboard: Try, try again. And always he looked grimmer and more determined than before. The strange thing was that, with all his industry and determination, he would break out every now and then into drunkenness, and lie round the tavern at the crossroads, and the school would be shut for two days. Then he came back, more fiercely resolute than ever. Even children could see that the mans life was a fight. It was like the battle between Good and Evil in Miltons epics. Well, after he had tried it four times, the schoolmaster at last passed the examination; and he went away to the city in a suit of store clothes, with eight hundred dollars that he had saved up, to study medicine. Now it happened that he had a brother who was not a bit like himself, but was a sort of neer-do-well, always hard-up and sponging on other people, and never working. And when the schoolmaster came to the city and his brother knew that he had eight hundred dollars, he came to him and got him drinking and persuaded him to hand over the eight hundred dollars and to let him put it into the Louisiana State lottery. In those days the Louisiana Lottery had not yet been forbidden the use of the mails, and you could buy a ticket for anything from one dollar up. The Grand Prize was two hundred thousand dollars, and the Seconds were a hundred thousand each. So the brother persuaded the schoolmaster to put the money in. He said he had a system for buying only the tickets with prime numbers, that wont divide by anything, and that it must win. He said it was a mathematical certainty, and he figured it ou with the schoolmaster in the back room of a saloon, with a box of dominoes on the table to show the plan of it. He told the
//		 schoolmaster that he himself would only take ten per cent of what
//		 they made, as a commission for showing the system, and the
//		 schoolmaster could have the rest. So, in a mad moment, the
//		 schoolmaster handed over his roll of money, and that was the last he
//		 ever saw of it. The next morning when he was up he was fierce with
//		 rage and remorse for what he had done. He could not go back to the
//		 school, and he had no money to go forward. So he stayed where he was
//		 in the little hotel where he had got drunk, and went on drinking. He
//		 looked so fierce and unkempt that in the hotel they were afraid of
//		 him, and the bar-tenders watched him out of the corners of their eyes
//		 wondering what he would do; because they knew that there was only one
//		 end possible, and they waited for it to come. And presently it came.
//		 One of the bar-tenders went up to the schoolmasters room to bring up
//		 a letter, and he found him lying on the bed with his face grey as
//		 ashes, and his eyes looking up at the ceiling. He was stone dead.
//		 Life had beaten him. And the strange thing was that the letter that
//		 the bartender carried up that morning was from the management of the
//		 Louisiana Lottery. It contained a draft on New York, signed by the
//		 treasurer of the State of Louisiana, for two hundred thousand
//		 dollars. The schoolmaster had won the Grand Prize. The above story, I
//		 am afraid, is a little gloomy. I put it down merely for the moral it
//		 contained, and I became so absorbed in telling it that I almost
//		 forgot what the moral was that it was meant to convey. But I think
//		 the idea is that if the schoolmaster had long before abandoned the
//		 study of medicine, for which he was not fitted, and gone in, let us
//		 say, for playing the banjo, he might have become end-man in a
//		 minstrel show. Yes, that was it. Let me pass on to other elements in
//		 success. I suppose that anybody will admit that the peculiar quality
//		 that is called initiative--the ability to act promptly on ones own
//		 judgement--is a factor of the highest importance.";
		// datvi =
		// "Hỡi đồng bào cả nước: Tất cả mọi người đều sinh ra có quyền bình đẳng. Tạo hóa cho họ những quyền không ai có thể xâm phạm được; trong những quyền ấy, có quyền được sống, quyền tự do và quyền mưu cầu hạnh phúc.Lời bất hủ ấy ở trong bản Tuyên ngôn Độc lập năm 1776 của nước Mỹ. Suy rộng ra, câu ấy có ý nghĩa là: tất cả các dân tộc trên thế giới đều sinh ra bình đẳng, dân tộc nào cũng có quyền sống, quyền sung sướng và quyền tự do.Bản Tuyên ngôn Nhân quyền và Dân quyền của Cách mạng Pháp năm 1791 cũng nói: Người ta sinh ra tự do và bình đẳng về quyền lợi; và phải luôn luôn được tự do và bình đẳng về quyền lợi.Đó là những lẽ phải không ai chối cãi được.Thế mà hơn 80 năm nay, bọn thực dân Pháp lợi dụng lá cờ tự do, bình đẳng, bác ái, đến cướp đất nước ta, áp bức đồng bào ta. Hành động của chúng trái hẳn với nhân đạo và chính nghĩa.Về chính trị, chúng tuyệt đối không cho nhân dân ta một chút tự do dân chủ nào.Chúng thi hành những luật pháp dã man. Chúng lập ba chế độ khác nhau ở Trung, Nam, Bắc để ngăn cản việc thống nhất nước nhà của ta, để ngăn cản dân tộc ta đoàn kết.Chúng lập ra nhà tù nhiều hơn trường học. Chúng thẳng tay chém giết những người yêu nước thương nòi của ta. Chúng tắm các cuộc khởi nghĩa của ta trong những bể máu. Chúng ràng buộc dư luận, thi hành chính sách ngu dân.Chúng dùng thuốc phiện, rượu cồn để làm cho nòi giống ta suy nhược.Về kinh tế, chúng bóc lột dân ta đến xương tủy, khiến cho dân ta nghèo nàn, thiếu thốn, nước ta xơ xác, tiêu điều. Chúng cướp không ruộng đất, hầm mỏ, nguyên liệu.Chúng giữ độc quyền in giấy bạc, xuất cảng và nhập cảng.Chúng đặt ra hàng trăm thứ thuế vô lý, làm cho dân ta, nhất là dân cày và dân buôn trở nên bần cùng.Chúng không cho các nhà tư sản ta ngóc đầu lên. Chúng bóc lột công nhân ta một cách vô cùng tàn nhẫn.Mùa thu năm 1940, phát xít Nhật đến xâm lăng Đông Dương để mở thêm căn cứ đánh Đồng Minh, thì bọn thực dân Pháp quỳ gối đầu hàng, mở cửa nước ta rước Nhật. Từ đó dân ta chịu hai tầng xiềng xích: Pháp và Nhật. Từ đó dân ta càng cực khổ, nghèo nàn. Kết quả là cuối năm ngoái sang đầu năm nay, từ Quảng Trị đến Bắc kỳ, hơn hai triệu đồng bào ta bị chết đói.Ngày 9 tháng 3 năm nay, Nhật tước khí giới của quân đội Pháp. Bọn thực dân Pháp hoặc là bỏ chạy, hoặc là đầu hàng. Thế là chẳng những chúng không bảo hộ được ta, trái lại, trong 5 năm, chúng đã bán nước ta hai lần cho Nhật.Trước ngày 9 tháng 3, biết bao lần Việt Minh đã kêu gọi người Pháp liên minh để chống Nhật. Bọn thực dân Pháp đã không đáp ứng lại thẳng tay khủng bố Việt Minh hơn nữa. Thậm chí đến khi thua chạy, chúng còn nhẫn tâm giết nốt số đông tù chính trị ở Yên Bái và Cao Bằng.Tuy vậy, đối với người Pháp, đồng bào ta vẫn giữ một thái độ khoan hồng và nhân đạo. Sau cuộc biến động ngày 9 tháng 3, Việt Minh đã giúp cho nhiều người Pháp chạy qua biên thùy, lại cứu cho nhiều người Pháp ra khỏi nhà giam Nhật và bảo vệ tính mạng và tài sản cho họ.Sự thật là từ mùa thu năm 1940, nước ta đã thành thuộc địa của Nhật, chứ không phải thuộc địa của Pháp nữa. Khi Nhật hàng Đồng minh thì nhân dân cả nước ta đã nổi dậy giành chính quyền, lập nên nước Việt Nam Dân chủ Cộng hòa.Sự thật là dân ta lấy lại nước Việt Nam từ tay Nhật, chứ không phải từ tay Pháp.Pháp chạy, Nhật hàng, vua Bảo Đại thoái vị. Dân ta đã đánh đổ các xiềng xích thực dân gần 100 năm nay để gây dựng nên nước Việt Nam độc lập. Dân ta lại đánh đổ chế độ quân chủ mấy mươi thế kỷ mà lập nên chế độ Dân chủ Cộng hòa.Bởi thế cho nên, chúng tôi, lâm thời Chính phủ của nước Việt Nam mới, đại biểu cho toàn dân Việt Nam, tuyên bố thoát ly hẳn quan hệ với Pháp, xóa bỏ hết những hiệp ước mà Pháp đã ký về nước Việt Nam, xóa bỏ tất cả mọi đặc quyền của Pháp trên đất nước Việt Nam.Toàn dân Việt Nam, trên dưới một lòng kiên quyết chống lại âm mưu của bọn thực dân Pháp.Chúng tôi tin rằng các nước Đồng minh đã công nhận những nguyên tắc dân tộc bình đẳng ở các Hội nghị Têhêrăng và Cựu Kim Sơn, quyết không thể không công nhận quyền độc lập của dân Việt Nam.Một dân tộc đã gan góc chống ách nô lệ của Pháp hơn 80 năm nay, một dân tộc đã gan góc đứng về phe Đồng Minh chống phát xít mấy năm nay, dân tộc đó phải được tự do! Dân tộc đó phải được độc lập!Vì những lẽ trên, chúng tôi, chính phủ lâm thời của nước Việt Nam Dân chủ Cộng hòa, trịnh trọng tuyên bố với thế giới rằng:Nước Việt Nam có quyền hưởng tự do và độc lập, và sự thật đã thành một nước tự do độc lập. Toàn thể dân tộc Việt Nam quyết đem tất cả tinh thần và lực lượng, tính mạng và của cải để giữ vững quyền tự do, độc lập ấy.";
		// data1 =
		// encode("Although French, German, American and British pioneers have all been credited with theinvention of cinema, the British and the Germans played a relatively small role in itsworldwide exploitation. It was above all the French, followed closely by the Americans,who were the most passionate exporters of the new invention, helping to start cinema inChina, Japan, Latin America and Russia. In terms of artistic development it was againthe French and the Americans who took the lead Although French, German, American and British pioneers have all been credited with theinvention of cinema, the British and the Germans played a relatively small role in itsworldwide exploitation. It was above all the French, followed closely by the Americans,who were the most passionate exporters of the new invention, helping to start cinema inChina, Japan, Latin America and Russia. In terms of artistic development it was againthe French and the Americans who took the leadAlthough French, German, American and British pioneers have all been credited with theinvention of cinema, the British and the Germans played a relatively small role in itsworldwide exploitation. It was above all the French, followed closely by the Americans,who were the most passionate exporters of the new invention, helping to start cinema inChina, Japan, Latin America and Russia. In terms of artistic development it was againthe French and the Americans who took the lead, though in the years before the FirstWorld War, Italy, Denmark and Russia also played a part.In the end it was the United States that was to become, and remain, the largest singlemarket for films. By protecting their own market and pursuing a vigorous export policy,the Americans achieved a dominant position on the world market by the start of the FirstWorld War. The centre of filmmaking had moved westwards, to Hollywood, and it wasfilms from these new Hollywood studios that flooded onto the worldâ€™s film markets in theyears after the First World War, and have done so ever since. Faced with totalHollywood domination, few film industries proved competitive. The Italian industry,which had pioneered the feature film with spectacular films like â€œQuo Vadis?â€� (1913) andâ€œCabiriaâ€� (1914), almost collapsed. In Scandinavia, the Swedish cinema had a briefperiod of glory, notably with powerful epic films and comedies. Even the French cinemafound itself in a difficult position.");
		// data1 +=
		// encode("Someinterfaces define just constants and no methods. For example, the standardlibrary contains an interface SwingConstants that defines constants NORTH SOUTH HORIZONTALand so on. Any class that chooses to implement the SwingConstants interface automatically inherits these constants. Its methods can simply refer to NORTH rather than the morecumbersome SwingConstants.NORTH. However, this use of interfaces seems rather degenerate, and we do not recommend it.While each class can have only one superclass, classes can implement multiple interfaces.This gives you the maximum amount of flexibility in defining a classâ€™s behavior. Forexample, the Java programming language has an important interface build in interface");

		data1 = encode(datfrfile);
		i = data1.length() / 5880;
		for (int j = 0; j < i; j++)
			datastring.add(data1.substring(5880 * j, 5880 * j + 5879));
		datastring.add(data1.substring(5880 * i));
	}

	public static String toJAVA(String unicode) {
		String output = "";

		char[] charArray = unicode.toCharArray();

		for (int i = 0; i < charArray.length; ++i) {
			char a = charArray[i];

			if ((int) a > 255) {
				if (a == 'đ') {
					output += "\\u0111";
				} else if (a == 'Đ') {
					output += "\\u0110";
				} else if (a == 'ư') {
					output += "\\u01b0";
				} else if (a == 'ă') {
					output += "\\u0103";
				} else if (a == 'ũ') {
					output += "\\u0169";
				} else if (a == 'ơ') {
					output += "\\u01a1";
				} else if (a == 'ĩ') {
					output += "\\u0129";
				} else
					output += "\\u" + Integer.toHexString((int) a);
			} else {
				output += a;
			}
		}

		return output;
	}

	ArrayList<Mat> improc() {
		ArrayList<Mat> datamat = new ArrayList<>();
		for (int k = 0; k < datastring.size(); k++) {
			int count = 0;
			Mat m = Mat.zeros(h, w, CvType.CV_8UC3);
			// Mat m = new zero(h, w, CvType.CV_8UC3);
			// String data =
			// encode("Tao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡pTao lÃ  hiá»‡p gÃ , hiá»‡p hiá»‡p - hiá»‡p hiá»‡p hiá»‡p");

			String data = datastring.get(k);
			// Log.d("TAG", data.length()+"");
			// if(k==0)
			// {
			// if(data.charAt(0) == '0')
			// {
			// pudatabd(m, 0, 0, '1');
			// pudatabd(m, 10, 0, '1');
			// pudatabd(m, 0, 10, '1');
			// }
			// else
			// {
			// pudatabd(m, 0, 0, '0');
			// pudatabd(m, 10, 0, '0');
			// pudatabd(m, 0, 10, '0');
			// }
			// if(data.charAt(55) == '0')
			// {
			// pudatabd(m, 0, w+10, '1');
			// pudatabd(m, 10, w+10, '1');
			// pudatabd(m, 0, w, '1');
			// }
			// else
			// {
			// pudatabd(m, 0, w+10, '0');
			// pudatabd(m, 10, w+10, '0');
			// pudatabd(m, 0, w, '0');
			// }
			// if(data.length() >=5878)
			// {
			// if(data.charAt(5879) == '0')
			// {
			// pudatabd(m, h+10, w+10, '1');
			// pudatabd(m, h, w+10, '1');
			// pudatabd(m, h+10, w, '1');
			// }
			// else
			// {
			// pudatabd(m, h+10, w+10, '0');
			// pudatabd(m, h-10, w+10, '0');
			// pudatabd(m, h+10, w, '0');
			// }
			// }
			// if(data.charAt(5824) == '0')
			// {
			// pudatabd(m, h+10, 0, '1');
			// pudatabd(m, h, 0, '1');
			// pudatabd(m, h+10, 10, '1');
			// }
			// else
			// {
			// pudatabd(m, h+10, 0, '0');
			// pudatabd(m, h, 0, '0');
			// pudatabd(m, h+10, 10, '0');
			// }
			// }
			Log.d("TAG", i + "");
			for (int i = 0; i < h + 0; i += 11)
				for (int j = 0; j < w + 0; j += 11) {
					count++;
					if (count < data.length()) {
						pudata(m, i, j, data.charAt(count - 1));
					}
					// else
					// pudata(m, i, j, '0');
				}
			datamat.add(m);
		}
		return datamat;
	}

	public String encode(String str) {
		String result = "";
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			String tmps = (Integer.toBinaryString((int) str.toCharArray()[i]))
					.toString();
			if (tmps.length() < 8) {
				for (int j = 0; j < (8 - tmps.length()); j++)
					temp.append('0');
				temp.append(tmps);
			} else
				temp.append(tmps);
			result += temp.toString();
			int l = temp.length();
			temp.delete(0, l);
		}
		return result;
	}

	void pudata(Mat m, int r, int c, char k) {
		double[] black = { 5, 5, 5 }; // 0
		double[] white = { 229, 255, 204 };

		for (int i = r; i < r + 10; i++)
			for (int j = c; j < c + 10; j++) {
				if (k == '0') {
					// m.put(i, j, black);
					// m.put(i, j + 1, white);
					// m.put(r + 10, j, white);
				} else {

					m.put(i, j, white);
					// m.put(i, j + 1, black);
					// m.put(r + 10, j, black);
				}
			}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		start = System.currentTimeMillis();
		Toast.makeText(mtv, "Đang mã hóa dữ liệu... !", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected ArrayList<Mat> doInBackground(Void... params) {

		return improc();

	}

	@Override
	protected void onPostExecute(ArrayList<Mat> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		end = (float) (System.currentTimeMillis() - start) / 1000;
		TransmitActivity.list_encoded.add(bpend2);
		for (int i = 0; i < result.size(); i++) {
			Mat m = result.get(i);
			Bitmap bmp = Bitmap
					.createBitmap(m.cols(), m.rows(), Config.RGB_565);
			Utils.matToBitmap(m, bmp);
			TransmitActivity.list_encoded.add(bmp);
			try {
				File sdCardDirectory = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + "Encode data");
				sdCardDirectory.mkdirs();

				String imageNameForSDCard = "data_" + String.valueOf(i + 1)
						+ ".jpg";

				File image = new File(sdCardDirectory, imageNameForSDCard);
				FileOutputStream outStream;

				outStream = new FileOutputStream(image);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				/* 100 to keep full quality of the image */
				outStream.flush();
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		TransmitActivity.list_encoded.add(bmpend);
		Toast.makeText(
				mtv,
				"Mã hóa xong!\n" + "Kích thước văn bản  : "
						+ datfrfile.length() + " kí tự.\nMã hóa thành : "
						+ String.valueOf(TransmitActivity.list_encoded.size() - 2)
						+ " ảnh.\nThời gian mã hóa : "
						+ String.format("%.2f", end) + " s.", Toast.LENGTH_LONG)
				.show();

		Log.d("TAG",
				"Mã hóa xong!\n" + "Kích thước văn bản  : "
						+ datfrfile.length() + " kí tự.\nMã hóa thành : "
						+ String.valueOf(TransmitActivity.list_encoded.size() - 2)
						+ " ảnh.\nThời gian mã hóa : " + end + " s.");
	}
}
