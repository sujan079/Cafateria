const http=require('http')
const app=require('./app')
const socket=require("socket.io");

const server=http.createServer(app);

const PORT=process.env.PORT||3000;

server.listen(PORT,()=>{
    console.log(`Server Running At ${PORT}`);
})

var io=socket(server);


io.on('connection',(socket)=>{
    console.log("Connection made",socket.id)


    socket.on("print",(data)=>{
        console.log("print command"+data);
        
        
        socket.broadcast.emit('print',data);

    })

    socket.on('disconnect',()=>{
        console.log("disconnect socket"+socket.id)
        socket.disconnect(true)
        
    })
})




