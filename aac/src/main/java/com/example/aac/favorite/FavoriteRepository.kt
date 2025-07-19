package com.example.aac.favorite

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FavoriteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val uid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun addFavorite(context: Context, item: FavoriteItem) {
        val userId = uid
        if (userId == null) {
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "label" to item.label,
            "iconResId" to item.iconResId
        )

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "${item.label}이(가) 즐겨찾기에 추가됨", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "추가 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeFavorite(context: Context, item: FavoriteItem) {
        val userId = uid
        if (userId == null) {
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .whereEqualTo("label", item.label)
            .whereEqualTo("iconResId", item.iconResId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.delete()
                }
                Toast.makeText(context, "${item.label}이(가) 삭제됨", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "삭제 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun clearFavorites(context: Context) {
        val userId = uid
        if (userId == null) {
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.delete()
                }
                Toast.makeText(context, "즐겨찾기를 모두 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "전체 삭제 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
