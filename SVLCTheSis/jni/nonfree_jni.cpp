#include <jni.h>
#include <android/log.h>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/nonfree/features2d.hpp>
#include <opencv2/nonfree/nonfree.hpp>
#include <opencv2/legacy/legacy.hpp>
#include <iostream>

using namespace cv;
using namespace std;

#define  LOG_TAG    "SVLC _ HiepTran _ JNI called"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#define CHECK_MAT(cond) if(!(cond)){ LOGI("FAILED: " #cond); return; }

#define MAXSIZE 512
#define RESIZE_FACTOR 0.125

typedef unsigned char uchar;

int get_SIFT(char * imgInFile, vector<KeyPoint>& keypoints, Mat& descriptors,
		int minhessian);
void Mat_to_vector_KeyPoint(Mat& mat, vector<KeyPoint>& v_kp);
void vector_KeyPoint_to_Mat(vector<KeyPoint>& v_kp, Mat& mat);
void vector_Point2f_to_Mat(vector<Point2f>& v_point, Mat& mat);
int getqkey;
int gettkey;
int getmatc;
extern "C" {
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_getquerykey(JNIEnv * env, jobject);
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_gettrainkey(JNIEnv * env, jobject);
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_getmatches(JNIEnv * env, jobject);
JNIEXPORT void JNICALL Java_com_svlcthesis_activity_RecoverData_getSIFT(JNIEnv * env, jobject, jstring addrImgInFile, jlong addrKeypoints,jlong addrDescriptors,jint minhessian);
JNIEXPORT void JNICALL Java_com_svlcthesis_activity_RecoverData_getCompare(JNIEnv * env, jobject, jlong addrRGBgoc,jlong addrRGBtrain,jlong addrCorrner);
};
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_getquerykey(JNIEnv * env, jobject)
{
	return getqkey;
}
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_gettrainkey(JNIEnv * env, jobject)
{
	return gettkey;
}
JNIEXPORT int JNICALL Java_com_svlcthesis_activity_RecoverData_getmatches(JNIEnv * env, jobject)
{
	return getmatc;
}
JNIEXPORT void JNICALL Java_com_svlcthesis_activity_RecoverData_getCompare(JNIEnv * env, jobject,  jlong addrRGBgoc,jlong addrRGBtrain,jlong addrCorrner)
{
LOGI("Call getCompare\n");
//int& mkeyquery = *(int*) keyquery;
//int& mkeytrain = *(int*) keytrain;
//int& mmatches = *(int*) amatches;
Mat& rgbquery = *(Mat*) addrRGBgoc;
Mat& rgbtrain = *(Mat*) addrRGBtrain;
Mat& corrner =  *(Mat*) addrCorrner;
//vector of keypoints
vector< cv::KeyPoint > keypointsQuery; //keypoints for object
vector< cv::KeyPoint > keypointsTrain; //keypoints for scene

//Descriptor matrices
Mat descriptors_query, descriptors_train;
SurfFeatureDetector surf(800);
LOGI( "Start get_SIFT! \n");
surf.detect(rgbquery,keypointsQuery);
SurfFeatureDetector surf2(3000);
surf2.detect(rgbtrain,keypointsTrain);

LOGI( "END get_SIFT! \n");
SurfDescriptorExtractor extractor;
surf.compute( rgbquery, keypointsQuery, descriptors_query );
surf2.compute( rgbtrain, keypointsTrain, descriptors_train );
getqkey = (int)keypointsQuery.size();
gettkey= (int)keypointsTrain.size();
LOGI("Query %d keypoints\n", (int)keypointsQuery.size());
LOGI("Train %d keypoints\n", (int)keypointsTrain.size());
LOGI( "END Compute! \n");
//Declering flann based matcher
FlannBasedMatcher matcher;
     vector< DMatch > matches;
     matcher.match( descriptors_query, descriptors_train, matches );
     LOGI("END Match! \n");
double max_dist = 0; double min_dist = 100;

          //-- Quick calculation of max and min distances between keypoints
          for( int i = 0; i < descriptors_query.rows; i++ )
          { double dist = matches[i].distance;
            if( dist < min_dist ) min_dist = dist;
            if( dist > max_dist ) max_dist = dist;
          }
          vector< DMatch > good_matches;

               for( int i = 0; i < descriptors_query.rows; i++ )
               { if( matches[i].distance < 3*min_dist )
                  { good_matches.push_back( matches[i]); }
               }



               //-- Localize the object
               vector<Point2f> obj;
               vector<Point2f> scene;

               for( int i = 0; i < good_matches.size(); i++ )
               {
                 //-- Get the keypoints from the good matches
                 obj.push_back( keypointsQuery[ good_matches[i].queryIdx ].pt );
                 scene.push_back( keypointsTrain[ good_matches[i].trainIdx ].pt );
               }
               getmatc =(int)good_matches.size();
         //      Mat H = findHomography( obj, scene, CV_RANSAC );

cv::Mat H = findHomography( cv::Mat(obj), cv::Mat(scene), CV_RANSAC );


//-- Get the corners from the image_1 ( the object to be "detected" )
std::vector< Point2f > obj_corners(4);
obj_corners[0] = cvPoint(0,0);
obj_corners[1] = cvPoint( rgbquery.cols, 0 );
obj_corners[2] = cvPoint( rgbquery.cols, rgbquery.rows );
obj_corners[3] = cvPoint( 0, rgbquery.rows );
std::vector< Point2f > scene_corners(4);
cv::perspectiveTransform( cv::Mat(obj_corners), cv::Mat(scene_corners), H);
LOGI("END get per! \n");
vector_Point2f_to_Mat(scene_corners,corrner);
LOGI("END convert! \n");
}

JNIEXPORT void JNICALL  Java_com_svlcthesis_activity_RecoverData_getSIFT(JNIEnv * env, jobject, jstring addrImgInFile, jlong addrKeypoints,jlong addrDescriptors,jint minhessian)
{

	LOGI( "Start 343443get variables! \n");

// get string to char*
const char* imgInFile_const = env->GetStringUTFChars(addrImgInFile, JNI_FALSE);
	char* imgInFile = new char[strlen(imgInFile_const)+1];
	strcpy(imgInFile, imgInFile_const);

	Mat& keypoints_mat = *(Mat*)addrKeypoints;
	vector<KeyPoint>* keypoints = new vector<KeyPoint>();
	Mat& descriptors = *(Mat*)addrDescriptors;
	// convert mat to vector keypoint
	LOGI( "%d \n", (int)keypoints_mat.cols);
// Mat_to_vector_KeyPoint(keypoints_mat, *keypoints);
LOGI( "Start 34343get_SIFT! \n");
get_SIFT(imgInFile, *keypoints,descriptors, minhessian);
	LOGI( "End 33344get_SIFT!\n");
// convert vector keypoint back to mat
vector_KeyPoint_to_Mat(*keypoints, keypoints_mat);
LOGI( "End 343434convertKeyPoint!\n");
// release the char*
env->ReleaseStringUTFChars(addrImgInFile, imgInFile_const);

// release the variables
delete []imgInFile;
delete keypoints;
}

int get_SIFT(char * imgInFile, vector<KeyPoint>& keypoints, Mat& descriptors,
	int minhessian) {
Mat query_image;
LOGI("%s \n", imgInFile);
query_image = imread(imgInFile, CV_LOAD_IMAGE_COLOR);
if (!query_image.data) {
	LOGI("Could not open or find the image!\n");
	return -1;
}

SurfFeatureDetector detector(400);
LOGI("Minhessian values %d \n", (int) minhessian);
detector.detect(query_image, keypoints);
LOGI("Detected %d keypoints\n", (int)keypoints.size());
detector.compute(query_image, keypoints, descriptors);
LOGI("Compute feature.\n");

//	// Store description to "descriptors.des".
//	FileStorage fs;
//	fs.open("descriptors.des", FileStorage::WRITE);
//	LOGI("Opened file to store the features.\n");
//	fs << "descriptors" << descriptors;
//	LOGI("Finished writing file.\n");
//	fs.release();
//	LOGI("Released file.\n");
LOGI("Done.\n");
return 0;
}
void vector_Point2f_to_Mat(vector<Point2f>& v_point, Mat& mat) {
mat = Mat(v_point, true);
}
void vector_KeyPoint_to_Mat(vector<KeyPoint>& v_kp, Mat& mat) {
int count = (int) v_kp.size();
mat.create(count, 1, CV_32FC(7));
for (int i = 0; i < count; i++) {
	KeyPoint kp = v_kp[i];
	mat.at < Vec<float, 7> > (i, 0) = Vec<float, 7>(kp.pt.x, kp.pt.y, kp.size,
			kp.angle, kp.response, (float) kp.octave, (float) kp.class_id);
}
}

