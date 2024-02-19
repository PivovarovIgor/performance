#include "employee_Employee.h"
#include <stdio.h>



JNIEXPORT void JNICALL Java_employee_Employee_raiseSalary
(JNIEnv* env, jobject this_obj, jdouble byPercent) {
    
    // 1.получить класс
    jclass class_Employee = (*env)->GetObjectClass(env, this_obj);
    
    // 2.получить идентификатор поля
    jfieldID id_salary = (*env)->GetFieldID(env, class_Employee, "salary", "D");
    
    // 3.получить значение поля
    jdouble salary = (*env)->GetDoubleField(env, this_obj, id_salary);
    
    salary *= 1 + byPercent / 100;
    
    // 4.установить значение поля
    (*env)->SetDoubleField(env, this_obj, id_salary, salary);
}
