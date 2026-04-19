Debugging Note: Vite Dev Server Showing Directory Listing Instead of React App
Summary
While trying to run the Bio Health Data Explorer React app with Vite, the dev server was showing a directory listing instead of rendering the app. The URL kept including an unwanted /Portfolio/ base path, causing 404 errors.

Issue
Running npm run dev opened a URL like http://localhost:5173/Portfolio/ which showed a 404.

Changing vite.config.ts to set a different port (e.g., 5500) did not change the base URL.

Accessing http://localhost:5500/ showed a directory listing instead of the React app.

Root Causes Found
Wrong working directory when running npm run dev
The package.json and index.html files live inside a nested folder:
/Users/jyliansummers/Desktop/WebDev Resume/PortfolioSite/BHDE/Bio Health/Data Explorer
but the dev server was started from /Users/jyliansummers/Desktop/WebDev Resume/PortfolioSite/BHDE, causing Vite to serve an unrelated directory.

The dev script was invoking Vite with --base /Portfolio/ due to a different package.json or environment configuration
The running package.json was not the expected one, or environment variables or npm configs were overriding the base.

Troubleshooting Steps
Verified current directory with pwd and folder contents with ls -l.

Noticed folder hierarchy with spaces in folder names, requiring quoting in terminal commands.

Correctly navigated into the exact folder containing the package.json and index.html:

bash
Copy
Edit
cd "Bio Health/Data Explorer"
Confirmed vite.config.ts was properly configured with the desired port and base settings.

Restarted the dev server and opened the URL printed by Vite (e.g., http://localhost:5500/).

Final Resolution
Running npm run dev from the correct folder containing package.json and index.html ensured Vite served the right files, and the React app loaded correctly at http://localhost:5500/.

Lessons Learned
Always verify the current working directory when running npm or build commands.

Use quotes or escape spaces in folder names when navigating via terminal.

Ensure the correct package.json and config files are being used by the dev server.

Vite’s base path (--base) can cause unexpected 404s if misconfigured or overridden.
