package main

import (
	"fmt"
	"net"
	"os"
)

func main() {
	// Define the server address and port
	serverAddress := "127.0.0.1:8170"

	// Resolve the address
	conn, err := net.Dial("tcp", serverAddress)

	if err != nil {
		fmt.Printf("Error connecting to server: %s\n", err.Error())
		os.Exit(1)
	}

	defer conn.Close()

	// Define the message to send
	message := "./printf.so"

	// Send the message
	_, err = conn.Write([]byte(message))

	if err != nil {
		fmt.Printf("Error sending message: %s\n", err.Error())
		os.Exit(1)
	}

	fmt.Println("Message sent to server!")
}
