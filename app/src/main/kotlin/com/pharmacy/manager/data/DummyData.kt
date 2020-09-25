package com.pharmacy.manager.data

import com.pharmacy.manager.components.chat.model.TempProduct
import com.pharmacy.manager.components.chatList.model.TempChat

object DummyData {

    val chatList = arrayListOf(
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 1",
            "Last user message 1",
            5,
            "12:29"
        ),
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 2",
            "Last user message long 2 Last user message long 2 Last user message long 2",
            1,
            "1:29"
        ),
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 3",
            "Last user message 3",
            10,
            "12:00"
        ),
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 4",
            "Last user message 4",
            1,
            "12:29"
        ),
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 5",
            "Last user message 5",
            1,
            "12:29"
        ),
        TempChat(
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg",
            "Test User 6",
            "Last user message 6",
            1,
            "12:29"
        )
    )

    val userResponses = arrayListOf(
        "Да, от лимона у меня краснеют щеки и от мяты глаза чешуться и слезяться иногда",
        "Чихаю периодически",
        "Горло красное и першит",
        "В легких скопилась мокрота",
        "Еще какой то странный текст с симптомами болезни"
    )

    val products = (1..15).map {
        TempProduct(
            "Рецепт 123",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта $it",
            "Таблетки шипучие, 24 шт",
            "568 ₽"
        )
    }
}