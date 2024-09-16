package main

import (
	"encoding/binary"
	"fmt"
	"net"
	"os"
)

type Packet struct {
	length  uint32
	payload []byte
}

func main() {
	args := os.Args
	argv := len(args)

	if argv != 4 {
		fmt.Printf("USAGE: %s <ip> <port> <module_path>\n", args[0])
		os.Exit(1)
	}

	serverAddress := args[1] + ":" + args[2]

	// Resolve the address
	conn, err := net.Dial("tcp", serverAddress)

	if err != nil {
		fmt.Printf("Error connecting to server: %s\n", err.Error())
		os.Exit(1)
	}

	defer conn.Close()

	packet := Packet{
		uint32(len(args[3])),
		[]byte(args[3]),
	}

	err = sendPacket(conn, packet)

	if err != nil {
		fmt.Printf("Error sending message: %s\n", err.Error())
		os.Exit(1)
	}

	fmt.Println("Message sent to server!")
}

func sendPacket(conn net.Conn, packet Packet) error {
	buf := make([]byte, 4+len(packet.payload))

	binary.BigEndian.PutUint32(buf[:4], uint32(packet.length))

	copy(buf[4:], packet.payload)

	_, err := conn.Write(buf)
	return err
}
