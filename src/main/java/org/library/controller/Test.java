package org.library.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class Test
{

	@GetMapping("/t")
	public void getTest()
	{
		System.out.println("get request");
	}
	@GetMapping
	public void getTest2()
	{
		System.out.println("get request");
	}
}

//libraryController,libraryModel,libraryService