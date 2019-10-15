const mongoose=require('mongoose');

const menuItemCategories=mongoose.Schema({
    _id:mongoose.Schema.Types.ObjectId,
    category:String
})

module.exports=mongoose.model('menu-item-catogeries',menuItemCategories);