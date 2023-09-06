import * as React from 'react';
import {Button, Stack} from "@mui/material";

const SongUploader = ({setSelectedFile}) => {
    const [isFilePicked, setIsFilePicked] = React.useState(false);

    function onFileUpload(event){
        setSelectedFile(event.target.files[0]);
        setIsFilePicked(true);
    }
    return (
        <Stack direction="row" alignItems="center" spacing={4}>
            <Button variant="contained" component="label" >
                <input accept="*" multiple type="file"  onChange={onFileUpload}/>
            </Button>
        </Stack>

    );


}

export default SongUploader;