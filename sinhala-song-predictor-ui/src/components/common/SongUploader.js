import * as React from 'react';
import {Button, Stack} from "@mui/material";

const SongUploader = () => {
    return (
        <Stack direction="row" alignItems="center" spacing={2}>
            <Button variant="contained" component="label">
                Upload
                <input hidden accept="audio/MP3" multiple type="file" />
            </Button>
        </Stack>

    );


}

export default SongUploader;